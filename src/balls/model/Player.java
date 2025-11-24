package balls.model;

import balls.dto.ShipPhysicsEngine;

import java.awt.*;

import static java.lang.Thread.sleep;

public class Player implements Runnable {

    private final Model model;
    private final ShipPhysicsEngine shipPhysicsEngine;
    private final Dimension size;
    private double rotationAngle;
    private Thread thread;
    private boolean running;

    public Player(Model model) {
        this.model = model;
        this.shipPhysicsEngine = new ShipPhysicsEngine();
        this.size = new Dimension(30, 50);
        this.rotationAngle = 0;

    }

    public void startThread() {
        this.running = true;
        this.thread = new Thread(this);
        this.thread.setName("Player thread");
        this.thread.start();
    }

    public void stopThread() {
        this.running = false;
        thread.interrupt();

        try {
            thread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        thread = null;
    }

    public Dimension getPostion() {
        return this.shipPhysicsEngine.getPosition();
    }

    public Dimension getSize() {
        return this.size;
    }

    public double getRotationAngle() {
        return this.rotationAngle;
    }

    // ----------------------------------- MOVEMENT -----------------------------------

    public void setMovingUp(boolean b) {
        this.shipPhysicsEngine.setMotorsPushingUp(b);
    }

    public void setMovingDown(boolean b) {
        this.shipPhysicsEngine.setMotorsPushingDown(b);
    }

    public void setMovingLeft(boolean b) {
        this.shipPhysicsEngine.setMotorsPushingLeft(b);
    }

    public void setMovingRight(boolean b) {
        this.shipPhysicsEngine.setMotorsPushingRight(b);
    }

    @Override
    public void run() {
        while (running) {
            Dimension[] nextPhyValues = shipPhysicsEngine.calculateNextPhysicalValues();
            shipPhysicsEngine.setPosition(nextPhyValues[0]);
            shipPhysicsEngine.setSpeed(nextPhyValues[1]);
            shipPhysicsEngine.setAcceleration(nextPhyValues[2]);
            this.rotationAngle = calcRotation();

            try {
                sleep(30);
            } catch (InterruptedException ignored) {}
        }
    }

    public double calcRotation() {
        double px = shipPhysicsEngine.getPosition().width;
        double py = shipPhysicsEngine.getPosition().height;

        double mouseX = model.getCursorPositionInViewer().width;
        double mouseY = model.getCursorPositionInViewer().height;

        double dx = mouseX - px;
        double dy = mouseY - py;

        return Math.atan2(dy, dx);
    }

}
