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
    private boolean isRunning;
    private boolean isPaused;
    private long lastSecondMillisecond;
    private int timesIteratedInLastSecond;

    public Viewer(View view) {
        this.view = view;
        this.isRunning = false;
        this.isPaused = true;

        setBackground(Color.WHITE);

        thread = new Thread(this);
    }

    @Override
    public void run() {
        view.addRoom(new Position(50, 50), new Dimension(150, 120));
        this.lastSecondMillisecond = System.currentTimeMillis();

        while (isRunning) {

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
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            timesIteratedInLastSecond += 1;
            updateDataPanelIfShould();

        }
    }
    public void startViewer() {
        thread.start();
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

    }
    private void clearRooms() {

    }
}
