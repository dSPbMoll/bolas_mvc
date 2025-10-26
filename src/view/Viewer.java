package view;

import dto.Position;
import model.Ball;
import model.Room;

import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class Viewer extends Canvas implements Runnable {
    private Thread thread;
    private final View view;
    private volatile boolean isRunning=false;
    private boolean isPaused;
    private volatile boolean running=false;

    public Viewer(View view) {
        this.view = view;
        setBackground(Color.WHITE);
    }

    @Override
    public void run() {

        while (running) {
            if(isRunning){
            Graphics2D g = (Graphics2D) getGraphics();
            if (g != null) {
                Graphics2D g2 = (Graphics2D) g;

                // Paint background (clear the frame)
                g2.setColor(getBackground());
                g2.fillRect(0, 0, getWidth(), getHeight());

                // Draw room
                paintRectangle(g);

                // Draw all balls
                CopyOnWriteArrayList<Ball> balls = view.getAllBalls();
                for (Ball ball : balls) {
                    paintBall(ball, g);
                }


                g2.dispose();
            }

            try {
                Thread.sleep(10); // velocidad de animación
            } catch (InterruptedException e) {
                break;
            }
        } else{
            try{
                Thread.sleep(1000);
            } catch (InterruptedException e){
                break;
            }
            }
        }
    }
    public void startViewer() {
        if(thread!=null && thread.isAlive()){
            stopViewer();
        }
        if(view.getAllRooms().isEmpty()){
            view.addRoom(new Position(50, 50), new Dimension(150, 120));
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
        startViewer();
    }
}
