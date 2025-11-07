package balls.view;

import balls.dto.Position;
import balls.model.Ball;
import balls.model.Room;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class Viewer extends Canvas implements Runnable {
    private Thread thread;
    private final View view;
    private volatile boolean isRunning=false;
    private boolean isPaused;
    private long lastSecondMillisecond;
    private int timesIteratedInLastSecond;
    private volatile boolean running=false;

    public Viewer(View view) {
        this.view = view;
        setBackground(Color.WHITE);
    }

    @Override
    public void run() {
        this.lastSecondMillisecond = System.currentTimeMillis();

        while (running) {
            if(isRunning){
                BufferStrategy bs= getBufferStrategy();
                if(bs==null){
                    if(isDisplayable()){
                        try{
                            createBufferStrategy(2);
                            bs=getBufferStrategy();
                        }catch(IllegalStateException ise){
                            try {
                                Thread.sleep(10);
                            } catch (InterruptedException e) {
                                break;
                            }
                        }
                    } else{
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            break;
                        }
                    }
                Graphics g=null;
                try{
                    g=bs.getDrawGraphics();
                    if(g!=null) {
                        Graphics2D g2 = (Graphics2D) g;

                        // Paint background (clear the frame)

                        g2.setColor(getBackground());
                        g2.fillRect(0, 0, getWidth(), getHeight());

                        // Draw room
                        paintRectangle(g2);

                        // Draw all balls
                        CopyOnWriteArrayList<Ball> balls = view.getAllBalls();
                        for (Ball ball : balls) {
                            paintBall(ball, g2);
                        }
                    }
                }finally {
                    if(g!=null){
                        g.dispose();
                    }
                    try{
                        bs.show();
                    }catch (Exception ignored){}
                }
            }
                try{
                    Thread.sleep(1000);
                } catch (InterruptedException e){
                    break;
                }
            } else{
                try{
                    Thread.sleep(1000);
                } catch (InterruptedException e){
                    break;
                }
            }

            timesIteratedInLastSecond += 1;
            updateDataPanelIfShould();

        }
    }
    public void startViewer() {
        if(thread!=null && thread.isAlive()){
            stopViewer();
        }
        if(view.getAllRooms().isEmpty()){
            view.addRoom(new Position(50, 50), new Dimension(150, 120));
            view.addRoom(new Position(100, 300), new Dimension(150, 120));
        }
        running=true;
        isRunning=true;
        thread=new Thread(this);
        thread.start();
    }

    public void stopViewer(){
        isRunning=false;
        running=false;
        if(thread!=null){
            thread.interrupt();
            try{
                thread.join();
            }catch (InterruptedException e){
                Thread.currentThread().interrupt();
            }
            thread=null;
        }
        BufferStrategy bs=getBufferStrategy();
        if(bs!=null){
            try{

            }catch (Exception ignored){}
        }
    }

    public void pauseViewer(){
        isRunning=false;
    }
    public void resumeViewer(){
        isRunning=true;
    }
    public Thread getThread() {
        return this.thread;
    }
    public void setIsRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }
    public boolean getIsRunning() {
        return this.isRunning;
    }
    public void paintBall(Ball ball, Graphics2D g) {
        int diameter = ball.getDIAMETER();
        int radius = Math.round(diameter/2);
        Position topLeftCornerOfBall = new Position(ball.getPosition().width - radius, ball.getPosition().height - radius);

        g.setColor(ball.getCOLOR());


        g.fillOval(topLeftCornerOfBall.width, topLeftCornerOfBall.height, diameter, diameter);
    }
    public void paintRectangle(Graphics2D g) {
        ArrayList<Room> rooms = view.getAllRooms();

        for (Room room : rooms) {
            Graphics2D gRectangle = (Graphics2D) g;
            if(room.getIsOccupied()){
                gRectangle.setColor(Color.RED);
                gRectangle.fillRect(room.getPosition().width, room.getPosition().height, room.getSize().width, room.getSize().height);
            }
            gRectangle.setColor(Color.BLUE);
            gRectangle.setStroke(new BasicStroke(3));
            gRectangle.drawRect(room.getPosition().width, room.getPosition().height, room.getSize().width, room.getSize().height);

        }

    }
    private void updateDataPanelIfShould() {
        if (this.lastSecondMillisecond +1000 < System.currentTimeMillis()) {
            //If the second has changed
            view.updateFPS(timesIteratedInLastSecond);
            view.updateRenderTime((double) Math.round((1000/timesIteratedInLastSecond) * 1000.0) / 1000.0);
            view.updateBallCount(view.getAllBalls().size());

            timesIteratedInLastSecond = 0;
            this.lastSecondMillisecond = System.currentTimeMillis();

        }
    }
    private void clearBalls() {
        view.getAllBalls().clear();
    }
    private void clearRooms() {
        view.getAllRooms().clear();
    }

    public void restartViewer(){
        stopViewer();
        view.stopAllBalls();
        clearBalls();
        clearRooms();
        view.addRoom(new Position(50, 50), new Dimension(150, 120));
        view.addRoom(new Position(100, 100), new Dimension(150, 120));
        startViewer();
    }
}
