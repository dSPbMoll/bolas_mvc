package asteroid.physics;

import java.awt.*;

public class ShipPhysicsEngine implements PhysicsEngine {

    private long lastUpdateMS;

    private double posX, posY;
    private double speedX, speedY;
    private double accX, accY;

    private double gravityX = 0;
    private double gravityY = 0;

    private final double motorForce = 300;   // push force by inputs
    private final double friction = 0.985;    // friction / resistance
    private final double maxSpeed = 750;     // speed limit

    private boolean motorsPushingUp;
    private boolean motorsPushingDown;
    private boolean motorsPushingLeft;
    private boolean motorsPushingRight;

    public ShipPhysicsEngine() {
        this.lastUpdateMS = System.currentTimeMillis();

        this.posX = 300;
        this.posY = 300;

        this.speedX = 0;
        this.speedY = 0;

        this.accX = 0;
        this.accY = 0;
    }

    // ------------------- GETTERS & SETTERS -------------------

    public void setLastUpdateMS(long setLastUpdateMS) { this.lastUpdateMS = setLastUpdateMS; }
    public long getLastUpdateMS() { return this.lastUpdateMS; }

    public void setPosition(Dimension position) {
        this.posX = position.getWidth();
        this.posY = position.getHeight();
    }

    public Dimension getPosition() {
        return new Dimension((int) posX, (int) posY);
    }

    public void setSpeed(Dimension speed) {
        this.speedX = speed.getWidth();
        this.speedY = speed.getHeight();
    }

    public Dimension getSpeed() {
        return new Dimension((int) speedX, (int) speedY);
    }

    public void setAcceleration(Dimension acceleration) {
        this.accX = acceleration.getWidth();
        this.accY = acceleration.getHeight();
    }

    public Dimension getAcceleration() {
        return new Dimension((int) accX, (int) accY);
    }

    public void setGravity(Dimension gravity) {
        this.gravityX = gravity.getWidth();
        this.gravityY = gravity.getHeight();
    }

    public Dimension getGravity() {
        return new Dimension((int) gravityX, (int) gravityY);
    }

    // ------------------- MOTORS -------------------

    public void setMotorsPushingUp(boolean b)    { this.motorsPushingUp = b; }
    public void setMotorsPushingDown(boolean b)  { this.motorsPushingDown = b; }
    public void setMotorsPushingLeft(boolean b)  { this.motorsPushingLeft = b; }
    public void setMotorsPushingRight(boolean b) { this.motorsPushingRight = b; }

    // ------------------- PHYSICS CALCULATIONS -------------------

    public Dimension[] calculateNextPhysicalValues() {

        long now = System.currentTimeMillis();
        double delta = (now - lastUpdateMS) / 1000.0;
        lastUpdateMS = now;

        //------------------- Acceleration by motors -------------------

        double accelX = gravityX;
        double accelY = gravityY;

        if (motorsPushingUp)    accelY -= motorForce;
        if (motorsPushingDown)  accelY += motorForce;
        if (motorsPushingLeft)  accelX -= motorForce;
        if (motorsPushingRight) accelX += motorForce;

        //------------------- Update speed -------------------

        double spdX = speedX;
        double spdY = speedY;

        spdX += accelX * delta;
        spdY += accelY * delta;

        //------------------- Apply friction -------------------

        spdX *= friction;
        spdY *= friction;

        //------------------- Limitate speed -------------------

        double speed = Math.sqrt(spdX * spdX + spdY * spdY);
        if (speed > maxSpeed) {
            double factor = maxSpeed / speed;
            spdX *= factor;
            spdY *= factor;
        }

        //------------------- Update position -------------------

        double positX = posX;
        double positY = posY;

        positX += spdX * delta;
        positY += spdY * delta;

        //------------------- Return Dimension[] -------------------

        Dimension[] result = new Dimension[3];
        result[0] = new Dimension((int)positX, (int)positY);
        result[1] = new Dimension((int)spdX, (int)spdY);
        result[2] = new Dimension((int)accelX, (int)accelY);
        return result;
    }
}
