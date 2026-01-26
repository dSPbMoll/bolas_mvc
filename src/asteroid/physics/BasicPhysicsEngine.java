package asteroid.physics;

import asteroid.dto.PhysicValuesDto;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.HashMap;

public class BasicPhysicsEngine extends PhysicsEngine {

    public BasicPhysicsEngine(Point2D.Double position, Point2D.Double speed, double rotationAngle, Point2D.Double acceleration, Point2D.Double gravity) {
        super(position, speed, rotationAngle);
        addVectorialPhysicalValue(VectorialPhysicalVariable.ACCELERATION, acceleration);
        addVectorialPhysicalValue(VectorialPhysicalVariable.GRAVITY, gravity);
    }

    @Override
    public PhysicValuesDto calculateNextPhysicalValues() {

        long newUpdateMS = System.currentTimeMillis();
        float deltaTimeSeconds = (newUpdateMS - this.lastUpdateMS) /1000.0f;
        Point2D.Double acceleration = getVectorialPhysicalValue(VectorialPhysicalVariable.ACCELERATION);
        Point2D.Double gravity = getVectorialPhysicalValue(VectorialPhysicalVariable.GRAVITY);
        Point2D.Double speed = getVectorialPhysicalValue(VectorialPhysicalVariable.SPEED);
        Point2D.Double position = getVectorialPhysicalValue(VectorialPhysicalVariable.POSITION);

        Point2D.Double accel = new Point2D.Double(
                acceleration.getX() * deltaTimeSeconds,
                acceleration.getY() * deltaTimeSeconds);

        // Combine gravity with accel
        double accelX = gravity.getX();
        double accelY = gravity.getY();
        accel.setLocation(accel.getX() + accelX, accel.getY() + accelY);

        // Apply acceleration to the speed
        double speedX = speed.x + accel.x;
        double speedY = speed.y + accel.y;
        Point2D.Double spd = new Point2D.Double(speedX, speedY);

        // Calculate new position based on the updated speed
        double pX = position.getX() + speedX;
        double pY = position.getY() + speedY;
        Point2D.Double pos = new Point2D.Double(pX, pY);

        //Prepare the physical values that will be returned
        HashMap<VectorialPhysicalVariable, Point2D.Double> phyVals = new HashMap<>();
        phyVals.put(VectorialPhysicalVariable.POSITION, pos);
        phyVals.put(VectorialPhysicalVariable.SPEED, spd);
        phyVals.put(VectorialPhysicalVariable.ACCELERATION, accel);
        phyVals.put(VectorialPhysicalVariable.GRAVITY, gravity);

        return new PhysicValuesDto(phyVals);
    }
}
