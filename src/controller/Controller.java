package controller;

import model.Ball;
import model.Room;
import view.View;
import model.Model;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class Controller {
    private final Model model;
    private final View view;

    public Controller() {
        this.model = new Model(this);
        this.view = new View(this);
    }
    public void addBall() {
        model.addBall();
    }
    public CopyOnWriteArrayList<Ball> getAllBalls() {
        return model.getAllBalls();
    }
    public int getViewerWidth() {
        return view.getViewerWidth();
    }
    public int getViewerHeight() {
        return view.getViewerHeight();
    }
    public void addRoom(int x, int y, int width, int height) {
        model.addRoom(x, y, width, height);
    }
}
