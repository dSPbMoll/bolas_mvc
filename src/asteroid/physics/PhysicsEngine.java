package asteroid.physics;

import asteroid.dto.PhysicValuesDto;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Map;

public abstract class PhysicsEngine implements PhysicsEngineInterface {
    protected long lastUpdateMS;
    protected HashMap<VectorialPhysicalVariable, Point2D.Double> vectorValues;
    protected HashMap<ScalarPhysicalVariable, Double> scalarValues;

    public PhysicsEngine(Point2D.Double position, Point2D.Double speed, double rotationAngle) {
        this.lastUpdateMS = System.currentTimeMillis();
        this.vectorValues = new HashMap<>();
        this.scalarValues = new HashMap<>();

        vectorValues.put(VectorialPhysicalVariable.POSITION, position);
        vectorValues.put(VectorialPhysicalVariable.SPEED, speed);

        scalarValues.put(ScalarPhysicalVariable.ROTATION_ANGLE, rotationAngle);
    }

    // ============================== GETTERS & SETTERS ==============================

    /**
     * Setter of the time in milliseconds where the last values update has been done
     * @param setLastUpdateMS time of last update in milliseconds
     */
    public void setLastUpdateMS(long setLastUpdateMS) {
        this.lastUpdateMS = setLastUpdateMS;
    }

    /**
     * Getter of last update in milliseconds
     * @return time of last update in milliseconds
     */
    public long getLastUpdateMS() {
        return this.lastUpdateMS;
    }

    /**
     * Update any engine's vectorial physical value
     * @param key name of the updated value
     * @param phyVal new value
     */
    public void setVectorialPhysicalValue(VectorialPhysicalVariable key, Point2D.Double phyVal) {
        try {
            if (!this.vectorValues.containsKey(key)) {
                throw new IllegalArgumentException("Key " + key + "does not exist in vectorValues's entries");
            }
        } catch(IllegalArgumentException e) {
            System.out.println(e);
        }
        this.vectorValues.put(key, phyVal);
    }

    /**
     * Update any engine's scalar physical value
     * @param key name of the updated value
     * @param phyVal new value
     */
    public void setScalarPhysicalValue(ScalarPhysicalVariable key, double phyVal) {
        try {
            if (!this.scalarValues.containsKey(key)) {
                throw new IllegalArgumentException("Key " + key + "does not exist in scalarValues's entries");
            }
        } catch(IllegalArgumentException e) {
            System.out.println(e);
        }
        this.scalarValues.put(key, phyVal);
    }

    /**
     * Update more than one vectorial phisical value at a time
     * @param phyVals HashMap with all the vectorial physical values that will be updated
     */
    public void setVectorialPhysicalValuesSet(HashMap<VectorialPhysicalVariable, Point2D.Double> phyVals) {
        if (phyVals == null) return;
        for (Map.Entry<VectorialPhysicalVariable, Point2D.Double> entry : phyVals.entrySet()) {
            setVectorialPhysicalValue(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Update more than one scalar phisical value at a time
     * @param phyVals HashMap with all the scalar physical values that will be updated
     */
    public void setScalarPhysicalValuesSet(HashMap<ScalarPhysicalVariable, Double> phyVals) {
        if (phyVals == null) return;
        for (Map.Entry<ScalarPhysicalVariable, Double> entry : phyVals.entrySet()) {
            setScalarPhysicalValue(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Get any engine's physical value
     * @param key name of the physic value
     * @return asked value
     */
    public Point2D.Double getVectorialPhysicalValue(VectorialPhysicalVariable key) {
        Point2D.Double phyVal = null;
        try {
            phyVal = this.vectorValues.get(key);
        } catch(NullPointerException e) {
            System.out.println(e);
        }
        return phyVal;
    }

    /**
     * Get any engine's scalar physical value
     * @param key name of the physic value
     * @return asked value
     */
    public Double getScalarPhysicalValue(ScalarPhysicalVariable key) {
        Double phyVal = null;
        try {
            phyVal = this.scalarValues.get(key);
        } catch(NullPointerException e) {
            System.out.println(e);
        }
        return phyVal;
    }

    // ============================= ENGINE METHODS ==============================

    /**
     * Create a new physical value for the engine
     * @param key identifier of the physical value (its name)
     * @param value value
     */
    public void addVectorialPhysicalValue(VectorialPhysicalVariable key, Point2D.Double value) {
        this.vectorValues.put(key, value);
    }

    /**
     * Create a new scalar physical value for the engine
     * @param key identifier of the physical value (its name)
     * @param value value
     */
    public void addScalarPhysicalValue(ScalarPhysicalVariable key, Double value) {
        this.scalarValues.put(key, value);
    }

    /**
     * Calculate the next physical values based on the previous ones
     * @return new physical values
     */
    public PhysicValuesDto calculateNextPhysicalValues() { // <===========================
        Point2D.Double position = vectorValues.get(VectorialPhysicalVariable.POSITION);
        Point2D.Double speed = vectorValues.get(VectorialPhysicalVariable.SPEED);

        long newUpdateMS = System.currentTimeMillis();
        float deltaTimeSeconds = (newUpdateMS - lastUpdateMS) / 1000.0f;

        this.lastUpdateMS = newUpdateMS;

        // Traveled distance
        int distanceX = (int) Math.round(speed.getX() * deltaTimeSeconds);
        int distanceY = (int) Math.round(speed.getY() * deltaTimeSeconds);

        // New position based on traveled distance
        int pX = (int) position.getX() + distanceX;
        int pY = (int) position.getY() + distanceY;
        Point2D.Double pos = new Point2D.Double(pX, pY);

        // Prepare the physical values that will be returned
        HashMap<VectorialPhysicalVariable, Point2D.Double> phyVals = new HashMap<>();
        phyVals.put(VectorialPhysicalVariable.POSITION, pos);
        phyVals.put(VectorialPhysicalVariable.SPEED, speed);

        return new PhysicValuesDto(phyVals);
    }
}
