package asteroid.physics;

import asteroid.dto.PhysicValuesDto;
import asteroid.dto.ShipMovementDto;
import helpers.CardinalDirection;

import java.awt.geom.Point2D;
import java.util.HashMap;

public class ShipPhysicsEngine extends BasicPhysicsEngine {
    protected final double motorForce = 300;   // push force by inputs
    //protected final double friction = 0.985;     friction / resistance
    protected final double maxSpeed = 750;     // speed limit

    protected boolean motorsPushingUp;
    protected boolean motorsPushingDown;
    protected boolean motorsPushingLeft;
    protected boolean motorsPushingRight;

    public ShipPhysicsEngine(Point2D.Double position, Point2D.Double speed, double rotationAngle, Point2D.Double acceleration, Point2D.Double gravity, double friction) {
        super(position, speed, rotationAngle, acceleration, gravity);
        
        scalarValues.put(ScalarPhysicalVariable.FRICTION, friction);
    }

    // ------------------- GETTERS & SETTERS -------------------


    // ------------------- MOTORS -------------------

    public void setMoving(boolean b, CardinalDirection direction) {
        switch(direction) {
            case CardinalDirection.NORTH -> this.motorsPushingUp = b;
            case CardinalDirection.SOUTH -> this.motorsPushingDown = b;
            case CardinalDirection.EAST -> this.motorsPushingRight = b;
            case CardinalDirection.WEST -> this.motorsPushingLeft = b;
        }
    }


    // ------------------- PHYSICS CALCULATIONS -------------------

    @Override
    public PhysicValuesDto calculateNextPhysicalValues() {
        long now = System.currentTimeMillis();
        double delta = (now - lastUpdateMS) / 1000.0;
        lastUpdateMS = now;

        Point2D.Double position = getVectorialPhysicalValue(VectorialPhysicalVariable.POSITION);
        Point2D.Double speed = getVectorialPhysicalValue(VectorialPhysicalVariable.SPEED);
        Point2D.Double acceleration = getVectorialPhysicalValue(VectorialPhysicalVariable.ACCELERATION);
        Point2D.Double gravity = getVectorialPhysicalValue(VectorialPhysicalVariable.GRAVITY);

        double friction = getScalarPhysicalValue(ScalarPhysicalVariable.FRICTION);

        //------------------- Acceleration by motors -------------------

        double accelX = gravity.getX();
        double accelY = gravity.getY();

        if (motorsPushingUp)    accelY -= motorForce;
        if (motorsPushingDown)  accelY += motorForce;
        if (motorsPushingLeft)  accelX -= motorForce;
        if (motorsPushingRight) accelX += motorForce;

        //------------------- Update speed -------------------

        double spdX = speed.getX();
        double spdY = speed.getY();

        spdX += accelX * delta;
        spdY += accelY * delta;

        //------------------- Apply friction -------------------

        spdX *= friction;
        spdY *= friction;

        //------------------- Limitate speed -------------------

        double spd = Math.sqrt(spdX * spdX + spdY * spdY);
        if (spd > maxSpeed) {
            double factor = maxSpeed / spd;
            spdX *= factor;
            spdY *= factor;
        }

        //------------------- Update position -------------------

        double posX = position.getX();
        double posY = position.getY();

        posX += spdX * delta;
        posY += spdY * delta;

        //------------------- Return -------------------

        HashMap<VectorialPhysicalVariable, Point2D.Double> vectPhyVals = new HashMap<>();
        vectPhyVals.put(VectorialPhysicalVariable.POSITION, new Point2D.Double(posX, posY));
        vectPhyVals.put(VectorialPhysicalVariable.SPEED, new Point2D.Double(spdX, spdY));
        vectPhyVals.put(VectorialPhysicalVariable.ACCELERATION, new Point2D.Double(accelX, accelY));

        return new PhysicValuesDto(vectPhyVals);
    }

    public ShipMovementDto getShipMovementDto(double rotationAngle) {
        return new ShipMovementDto(motorsPushingUp, motorsPushingDown, motorsPushingLeft, motorsPushingRight, rotationAngle);
    }
}
