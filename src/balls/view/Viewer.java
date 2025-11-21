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
    private long lastSecondMillisecond;
    private int timesIteratedInLastSecond;
    private volatile boolean running=false;
    private BufferStrategy bufferStrategy;

    public Viewer(View view) {
        this.view = view;
        setBackground(Color.WHITE);
        setIgnoreRepaint(true);
    }

    @Override
    public void run() {
        this.lastSecondMillisecond = System.currentTimeMillis();
        while(getBufferStrategy()==null){
            createBufferStrategy(2);
            bufferStrategy = getBufferStrategy();
        }
        while (running) {
            Graphics g=null;
            try {
                g = bufferStrategy.getDrawGraphics();
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
            } catch (Exception ignored) {
            } finally {
                if (g != null) g.dispose();
                bufferStrategy.show();
            }

            try{
                Thread.sleep(10);
            } catch (InterruptedException ignored){
            }
            timesIteratedInLastSecond += 1;
            updateDataPanelIfShould();
        }
    }
    public void startViewer() {

        if(view.getAllRooms().isEmpty()){
            view.addRoom(new Position(50, 50), new Dimension(150, 120));
            view.addRoom(new Position(100, 300), new Dimension(150, 120));
        }
        running = true;
        thread = new Thread(this);
        thread.start();
    }

    public void pauseViewer(){
        running = false;
        thread.interrupt();

        try {
            thread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        thread = null;
    }

    public void restartViewer(){
        view.stopAllBalls();
        clearBalls();
        clearRooms();
        pauseViewer();
        startViewer();
    }

    public Thread getThread() {
        return this.thread;
    }

    public void paintBall(Ball ball, Graphics2D g) {
        int diameter = ball.getDIAMETER();
        int radius = Math.round(diameter/2);
        Dimension topLeftCornerOfBall = new Dimension(ball.getPosition().width - radius, ball.getPosition().height - radius);

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

    public boolean getRunning() {
        return running;
    }
}
