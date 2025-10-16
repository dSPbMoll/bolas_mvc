package model;

import controller.Controller;

import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class Model {
    private final Controller controller;
    private CopyOnWriteArrayList<Ball> ballList;
    private ArrayList<Room> rectangleRoomList;

    public Model(Controller controller) {
        this.controller = controller;
        this.ballList = new CopyOnWriteArrayList<Ball>();
        this.rectangleRoomList = new ArrayList<>();
    }
    public void addBall() {
        Ball ball = new Ball(this);
        ballList.add(ball);
    }
    public void addRoom(int x, int y, int width, int height) {
        Room room = new Room(this, x, y, width, height);
        rectangleRoomList.add(room);
    }
    public CopyOnWriteArrayList<Ball> getAllBalls() {
        return ballList;
    }
    public int getViewerWidth() {
        return controller.getViewerWidth();
    }
    public int getViewerHeight() {
        return controller.getViewerHeight();
    }
    public boolean go(int attemptedX, int attemptedY) {
        for (Room room : rectangleRoomList) {
            if (room.go(attemptedX, attemptedY) && (room.goIn())) {
                return false;
            }
        }
        return true;
    }
}
