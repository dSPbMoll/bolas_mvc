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

    public Viewer(View view) {
        this.view = view;
        setBackground(Color.WHITE);

        thread = new Thread(this);
    }

    @Override
    public void run() {
        view.addRoom(new Position(50, 50), new Dimension(150, 120));
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
}
