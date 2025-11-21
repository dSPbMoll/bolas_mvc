package balls.dto;

public interface MovableObject {

    /**
     * Stablishes a new position for the Object
     * @param position New position
     */
    void setPosition(Position position);

    /**
     * @return The position of the object
     */
    Position getPosition();
}
