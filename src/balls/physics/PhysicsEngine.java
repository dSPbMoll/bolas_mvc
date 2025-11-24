package balls.physics;

import java.awt.*;

public interface PhysicsEngine {

    /**
     * Changes the value of Engine's location
     * @param position New position of the object
     */
    void setPosition(Dimension position);

    /**
     * @return The current position of the Object
     */
    Dimension getPosition();

    /**
     * Changes the value of Engine's speed
     * @param speed Units of space traveled per unit of time
     */
    void setSpeed(Dimension speed);

    /**
     * @return Current value of Engine's speed
     */
    Dimension getSpeed();

    /**
     * Changes the value of Engine's gravity
     * @param gravity Acceleration of the gravity
     */
    void setGravity(Dimension gravity);

    /**
     * @return Current value of Engine's gravity
     */
    Dimension getGravity();

    /**
     * Changes the value of Engine's acceleration
     * @param acceleration Incrementation / decrementation of the speed per second
     */
    void setAcceleration(Dimension acceleration);

    /**
     * @return Current value of Engine's acceleration
     */
    Dimension getAcceleration();

    /**
     * Calculates the next position of the Object that is being moved by the Engine
     * @return The next position that the object will take
     */
    Dimension[] calculateNextPhysicalValues();
}
