package model;

import dto.Position;

import java.awt.*;

public class Room {
    private boolean isOccupied;
    private Ball ballInside;
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
    public Ball getBallInside() {return this.ballInside;}
    public void setBallInside(Ball ball) {this.ballInside = ball;}
    public Position getPosition() {return this.position;}
    public Dimension getSize() {return this.size;}

    synchronized boolean enteringBall(Ball ball) {
        if (this.ballInside == null) {
            //If the room is empty

            setBallInside(ball);
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
