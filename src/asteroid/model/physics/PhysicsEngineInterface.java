package asteroid.model.physics;

import asteroid.dto.PhysicValuesDto;

import java.awt.geom.Point2D;

public interface PhysicsEngineInterface {

    /**
     * Setter of the time in milliseconds where the last values update has been done
     * @param setLastUpdateMS time of last update in milliseconds
     */
    void setLastUpdateMS(long setLastUpdateMS);

    /**
     * Getter of last update in milliseconds
     * @return time of last update in milliseconds
     */
    long getLastUpdateMS();

    /**
     * Update any engine's physical value
     * @param key name of the updated value
     * @param phyVal new value
     */
    void setVectorialPhysicalValue(VectorialPhysicalVariable key, Point2D.Double phyVal);

    /**
     * Update any engine's scalar physical value
     * @param key name of the updated value
     * @param phyVal new value
     */
    void setScalarPhysicalValue(ScalarPhysicalVariable key, double phyVal);

    /**
     * Get any engine's physical value
     * @param key name of the physic value
     * @return asked value
     */
    Point2D.Double getVectorialPhysicalValue(VectorialPhysicalVariable key);

    /**
     * Get any engine's scalar physical value
     * @param key name of the physic value
     * @return asked value
     */
    Double getScalarPhysicalValue(ScalarPhysicalVariable key);

    /**
     * Create a new physical value for the engine
     * @param key identifier of the physical value (its name)
     * @param value value
     */
    void addVectorialPhysicalValue(VectorialPhysicalVariable key, Point2D.Double value);

    /**
     * Create a new scalar physical value for the engine
     * @param key identifier of the physical value (its name)
     * @param value value
     */
    void addScalarPhysicalValue(ScalarPhysicalVariable key, Double value);

    /**
     * Calculates the next position of the Object that is being moved by the Engine
     * @return The next position that the object will take
     */
    PhysicValuesDto calculateNextPhysicalValues();
}
