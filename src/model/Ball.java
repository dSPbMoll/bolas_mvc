package model;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.Random;

import static java.lang.Math.abs;
import static java.lang.Thread.sleep;

public class Ball implements Runnable {
    private Model model;
    private int x, y, speedX, speedY;
    private final int DIAMETER;
    private final Color COLOR;

    public Ball(Model model) {
        this.model = model;
        x = (int) (Math.random() * model.getViewerWidth());
        y = (int) (Math.random() * model.getViewerHeight());
        speedX = (int) (2);
        speedY = (int) (2);
        DIAMETER = 15;
        COLOR = new Color(0,0,0);
        Thread thread = new Thread(this);
        thread.start();
    }
    public Color getCOLOR() {
        return this.COLOR;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public int getDIAMETER() {
        return this.DIAMETER;
    }

    @Override
    public void run() {
        while (true) {
            int attemptedX = x + speedX;
            int attemptedY = y + speedY;
            if (model.go(attemptedX, attemptedY)) {
                x = attemptedX;
                y = attemptedY;

                if (attemptedX <= 0) {
                    speedX = abs(speedX);
                } else if (attemptedX + DIAMETER >= model.getViewerWidth()) {
                    speedX = -abs(speedX);
                } else if (attemptedY <= 0) {
                    speedY = abs(speedY);
                } else if (attemptedY + DIAMETER >= model.getViewerHeight()) {
                    speedY = -abs(speedY);
                }
            }



            try {
                sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
