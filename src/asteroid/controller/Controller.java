package asteroid.controller;

import asteroid.model.Asteroid;
import asteroid.model.EventType;
import asteroid.view.View;
import asteroid.model.Model;

import java.awt.*;
import java.util.ArrayList;

public class Controller {
    private final Model model;
    private final View view;

    public Controller() {
        this.model = new Model(this);
        this.view = new View(this);

        this.model.addPlayer();
    }

    // ------------------------------- MODEL EVENTS MANAGING -------------------------------

    //public void asteroidEventManager(EventType event, Room room, Asteroid asteroid) {
        //switch (event) {
            //case ASTEROID_ENTERS_OCCUPIED_ROOM:
                //model.asteroidEntersOccupiedRoom(asteroid, room);
                //break;

            //case ASTEROID_ENTERS_FREE_ROOM:
                //model.asteroidEntersFreeRoom(asteroid, room);
                //break;

            //case ASTEROID_MOVES_INSIDE_ROOM:
                //model.asteroidMovesInsideRoom(asteroid, room);
                //break;

            //case ASTEROID_EXITS_ROOM:
                //model.asteroidExitsRoom(asteroid, room);
                //break;

        //}
    //}

    public void asteroidEventManager(EventType event, Asteroid asteroid) {
        switch (event) {
            case NORTH_LIMIT_REACHED:
                model.northLimitBounce(asteroid);
                break;

            case SOUTH_LIMIT_REACHED:
                model.southLimitBounce(asteroid);
                break;

            case EAST_LIMIT_REACHED:
                model.eastLimitBounce(asteroid);
                break;

            case WEST_LIMIT_REACHED:
                model.westLimitBounce(asteroid);
                break;

        }
    }

    // ------------------------------------- GETTERS & SETTERS -------------------------------------

    public void setPaused(boolean paused){
        model.setIsPaused(paused);
    }

    // ------------------------------------- LINKING METHODS -------------------------------------
    // ------------- ASTEROID

    public void addAsteroid() {
        model.addAsteroid();
    }

    public ArrayList<Asteroid> getAllAsteroids() {
        return model.getAllAsteroids();
    }

    public void stopAllAsteroids(){
        model.stopAllAsteroids();
    }

    public int getMinAsteroidSpeedSliderValue() {
        return view.getMinAsteroidSpeedSliderValue();
    }

    public int getMaxAsteroidSpeedSliderValue() {
        return view.getMaxAsteroidSpeedSliderValue();
    }

    public int getMinAsteroidSizeSliderValue() {
        return view.getMinAsteroidSizeSliderValue();
    }

    public int getMaxAsteroidSizeSliderValue() {
        return view.getMaxAsteroidSizeSliderValue();
    }

    // ------------ ROOM

    //public void addRoom(Position position, Dimension size) {
        //model.addRoom(position, size);
    //}

    //public ArrayList<Room> getAllRooms() {
        //return model.getAllRooms();
    //}

    // ------------- PLAYER

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

    public void startPlayerThread() {
        model.startPlayerThread();
    }

    public void stopPlayerThread() {
        model.stopPlayerThread();
    }

    // --------- OTHER

    public int getViewerWidth() {
        return view.getViewerWidth();
    }

    public int getViewerHeight() {
        return view.getViewerHeight();
    }
}
