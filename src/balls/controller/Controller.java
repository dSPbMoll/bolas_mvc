package balls.controller;

import balls.dto.Position;
import balls.model.Ball;
import balls.model.EventType;
import balls.model.Room;
import balls.view.View;
import balls.model.Model;

import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class Controller {
    private final Model model;
    private final View view;

    public Controller() {
        this.model = new Model(this);
        this.view = new View(this);

        this.model.addPlayer();
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
    public void stopAllBalls(){
        model.stopAllBalls();
    }
    public void setPaused(boolean paused){
        model.setPaused(paused);
    }

    public void startPlayerThread() {
        model.startPlayerThread();
    }

    public void stopPlayerThread() {
        model.stopPlayerThread();
    }

    public void ballEventManager(EventType event, Room room, Ball ball) {
        switch (event) {
            case BALL_ENTERS_OCCUPIED_ROOM:
                model.ballEntersOccupiedRoom(ball, room);
                break;

            case BALL_ENTERS_FREE_ROOM:
                model.ballEntersFreeRoom(ball, room);
                break;

            case BALL_MOVES_INSIDE_ROOM:
                model.ballMovesInsideRoom(ball, room);
                break;

            case BALL_EXITS_ROOM:
                model.ballExitsRoom(ball, room);
                break;

        }
    }
    public void ballEventManager(EventType event, Ball ball) {
        switch (event) {
            case NORTH_LIMIT_REACHED:
                model.northLimitBounce(ball);
                break;

            case SOUTH_LIMIT_REACHED:
                model.southLimitBounce(ball);
                break;

            case EAST_LIMIT_REACHED:
                model.eastLimitBounce(ball);
                break;

            case WEST_LIMIT_REACHED:
                model.westLimitBounce(ball);
                break;

        }
    }

    // -------------------------------- SHIP ACTIONS --------------------------------

    public Dimension getPlayerPosition() {
        return model.getPlayerPosition();
    }

    public Dimension getPlayerSize() {
        return this.model.getPlayerSize();
    }

    public void setPlayerMovingUp(boolean b) {
        model.setPlayerMovingUp(b);
    }

    public void setPlayerMovingLeft(boolean b) {
        model.setPlayerMovingLeft(b);
    }

    public void setPlayerMovingRight(boolean b) {
        model.setPlayerMovingRight(b);
    }

    public void setPlayerMovingDown(boolean b) {
        model.setPlayerMovingDown(b);
    }

    public Dimension getCursorPositionInViewer() {
        return view.getCursorPositionInViewer();
    }

    public double getPlayerRotationAngle() {
        return model.getPlayerRotationAngle();
    }
}
