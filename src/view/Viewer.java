package view;

import model.Ball;

import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class Viewer extends Canvas implements Runnable {
    private Thread thread;
    private final View view;

    public Viewer(View view) {
        this.view = view;
        setBackground(Color.WHITE);

        thread = new Thread(this);
    }

    @Override
    public void run() {
        view.addRoom(50, 50, 100, 60);
        while (true) {
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
                Thread.sleep(1); // velocidad de animación
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public void startViewer() {
        thread.start();
    }
    public Thread getThread() {
        return this.thread;
    }
    public void paintBall(Ball ball, Graphics2D g) {
        int diameter = ball.getDIAMETER();
        g.setColor(ball.getCOLOR());
        g.fillOval(ball.getX(), ball.getY(), diameter, diameter);
    }
    public void paintRectangle(Graphics2D g) {
        Graphics2D gRectangle = (Graphics2D) g;
        gRectangle.setColor(Color.BLUE);
        gRectangle.setStroke(new BasicStroke(3));
        gRectangle.drawRect(50, 50, 100, 60);
    }
}
