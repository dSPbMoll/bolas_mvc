package controller;

import dto.Position;
import model.Ball;
import model.Room;
import view.View;
import model.Model;

import java.awt.*;
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
    public void addRoom(Position position, Dimension size) {
        model.addRoom(position, size);
    }
    public int getMinBallSpeedSliderValue() {
        return view.getMinBallSpeedSliderValue();
    }
    public int getMaxBallSpeedSliderValue() {
        return view.getMaxBallSpeedSliderValue();
    }
    public int getMinBallSizeSliderValue() {
        return view.getMinBallSizeSliderValue();
    }
    public int getMaxBallSizeSliderValue() {
        return view.getMaxBallSizeSliderValue();
    }
    public ArrayList<Room> getAllRooms() {
        return model.getAllRooms();
    }
}
