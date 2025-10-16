package model;

import java.awt.*;

public class Room {
    private boolean isOccupied;
    private int x;
    private int y;
    private int width;
    private int height;

    public Room(Model model, int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }
    public boolean isOccupied() {
        return isOccupied;
    }

    public boolean go (int attemptedX, int attemptedY) {
        if ((attemptedX >= x && attemptedX <= x+width) && (attemptedY >= y && attemptedY <= y+height)) {
            return true;
        }
        return false;
    }
    synchronized public boolean goIn() {
        if (isOccupied == true) {
            return true;
        }
        return false;
    }
}
