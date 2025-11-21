package balls.dto;

import java.awt.*;

public class BasicPhysicsEngine implements PhysicsEngine {

    private long lastUpdateMS;
    private Dimension position;
    private Dimension speed;
    private Dimension gravity;
    private Dimension acceleration;

    public BasicPhysicsEngine() {
        this.lastUpdateMS = System.currentTimeMillis();
        this.position = new Dimension(0, 0);
        this.speed = new Dimension(0, 0);
        this.gravity = new Dimension(0, 0);
        this.acceleration = new Dimension(0, 0);
    }

    public void setLastUpdateMS(long setLastUpdateMS) {
        this.lastUpdateMS = setLastUpdateMS;
    }

    public long getLastUpdateMS() {
        return this.lastUpdateMS;
    }

    public void setPosition(Dimension position) {
        this.position = position;
    }

    public Dimension getPosition() {
        return this.position;
    }

    public void setSpeed(Dimension speed) {
        this.speed = speed;
    }

    public Dimension getSpeed() {
        return this.speed;
    }

    public void setGravity(Dimension gravity) {
        this.gravity = gravity;
    }

    public Dimension getGravity() {
        return this.gravity;
    }

    public void setAcceleration(Dimension acceleration) {
        this.acceleration = acceleration;
    }

    public Dimension getAcceleration() {
        return this.acceleration;
    }

    public Dimension[] calculateNextPhysicalValues() {

        long newUpdateMS = System.currentTimeMillis();
        float deltaTimeSeconds = (newUpdateMS - lastUpdateMS) /1000.0f;

        Dimension accel = new Dimension((int) Math.round(this.acceleration.getWidth() * deltaTimeSeconds),
                (int) Math.round(this.acceleration.getHeight() * deltaTimeSeconds));

        // Combine gravity with accel
        int accelX = (int) gravity.getWidth();
        int accelY = (int) gravity.getHeight();
        accel.setSize(accel.getWidth() + accelX, accel.getHeight() + accelY);

        // Apply acceleration to the speed
        int speedX = Math.round(speed.width + accel.width);
        int speedY = Math.round(speed.height + accel.height);
        Dimension spd = new Dimension(speedX, speedY);

        // Calculate new position based on the updated speed
        int pX = (int) position.getWidth() + speedX;
        int pY = (int) position.getHeight() + speedY;
        Dimension pos = new Dimension(pX, pY);

        //Prepare the physical values that will be given to the caller
        Dimension[] physicalValues = new Dimension[3];
        physicalValues[0] = pos;
        physicalValues[1] = spd;
        physicalValues[2] = accel;

        return physicalValues;
    }
}
