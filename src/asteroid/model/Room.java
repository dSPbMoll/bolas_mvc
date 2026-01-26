package asteroid.model;

import asteroid.dto.Position;
import asteroid.model.body.BodyAsteroid;

import java.awt.*;

public class Room {
    private boolean isOccupied;
    private BodyAsteroid asteroidInside;
    private Position position;
    private Dimension size;

    public Room(Model model, Position position, Dimension size) {
        this.isOccupied = false;
        this.position = position;
        this.size = size;
    }

    public boolean getIsOccupied() {
        return isOccupied;
    }
    public void setIsOccupied(boolean isOccupied) {this.isOccupied = isOccupied;}
    public BodyAsteroid getBallInside() {return this.asteroidInside;}
    public void setBallInside(BodyAsteroid asteroid) {this.asteroidInside = asteroid;}
    public Position getPosition() {return this.position;}
    public Dimension getSize() {return this.size;}

    synchronized boolean enteringBall(BodyAsteroid asteroid) {
        if (this.asteroidInside == null) {
            //If the room is empty

            setBallInside(asteroid);
            setIsOccupied(true);
            return true;

        } else {
            //If the room is not empty (occupied by other ball)

            try {
                while (this.isOccupied) {
                    wait();
                    return false;
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        return true;
    }
    synchronized boolean exitingBall() {
        setBallInside(null);
        setIsOccupied(false);
        notifyAll();
        return true;
    }
}
