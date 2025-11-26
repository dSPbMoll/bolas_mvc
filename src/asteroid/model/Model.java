package asteroid.model;

import asteroid.controller.Controller;

import java.awt.*;
import java.util.ArrayList;

import static java.lang.Math.abs;

public class Model {
    private final Controller controller;
    private ArrayList<Asteroid> asteroidList;
    //private ArrayList<Room> rectangleRoomList;
    private volatile boolean isPaused = false;
    private Player player;

    public Model(Controller controller) {
        this.controller = controller;
        this.asteroidList = new ArrayList<>();
        //this.rectangleRoomList = new ArrayList<>();
    }

    // ------------------------------------ GETTERS & SETTERS ------------------------------------

    public boolean getIsPaused(){
        return isPaused;
    }

    public void setIsPaused(boolean paused){
        this.isPaused =paused;
    }

    /*
    synchronized public ArrayList<Room> getAllRooms() {
        return this.rectangleRoomList;
    }

     */

    synchronized public ArrayList<Asteroid> getAllAsteroids() {
        return asteroidList;
    }

    // ------------------------------------ LINKING METHODS ------------------------------------

    public int getViewerWidth() {return controller.getViewerWidth();}

    public int getViewerHeight() {return controller.getViewerHeight();}

    public int getMinAsteroidSpeedSliderValue() {
        return controller.getMinAsteroidSpeedSliderValue();
    }

    public int getMaxAsteroidSpeedSliderValue() {
        return controller.getMaxAsteroidSpeedSliderValue();
    }

    public int getMinAsteroidSizeSliderValue() {
        return controller.getMinAsteroidSizeSliderValue();
    }

    public int getMaxAsteroidSizeSliderValue() {
        return controller.getMaxAsteroidSizeSliderValue();
    }

    // ------------- PLAYER

    public Dimension getPlayerPosition() {
        return this.player.getPostion();
    }

    public Dimension getPlayerSize() {
        return this.player.getSize();
    }

    public void addPlayer() {
        this.player = new Player(this);
    }

    public void startPlayerThread() {
        this.player.startThread();
    }

    public void stopPlayerThread() {
        this.player.stopThread();
    }

    public void setPlayerMovingUp(boolean b) {
        player.setMovingUp(b);
    }

    public void setPlayerMovingLeft(boolean b) {
        player.setMovingLeft(b);
    }

    public void setPlayerMovingRight(boolean b) {
        player.setMovingRight(b);
    }

    public void setPlayerMovingDown(boolean b) {
        player.setMovingDown(b);
    }

    public Dimension getCursorPositionInViewer() {
        return controller.getCursorPositionInViewer();
    }

    public double getPlayerRotationAngle() {
        return player.getRotationAngle();
    }

    // ------------------------------------ SIMULATION MODIFYING METHODS ------------------------------------

    synchronized public void addAsteroid() {
        Asteroid asteroid = new Asteroid(this);
        asteroid.startThread();
        asteroidList.add(asteroid);
    }

    public void stopAllAsteroids(){
        for (Asteroid a : asteroidList){
            a.stopThread();
        }
    }

    /*
    public void addRoom(Position position, Dimension size) {
        Room room = new Room(this, position, size);
        rectangleRoomList.add(room);
    }

     */

    // ------------------------------------ SIMULATION CONTROL METHODS ------------------------------------
    // ---------- GENERAL EVENT PROCESSING

    /**
     * @param asteroid is the asteroid that calls the method for moving
     * @param attemptedPosition is the position the asteroid who called the function is willing to take
     * @return true -> the asteroid is allowed to move; false -> the movement got denied;
     */
    synchronized public void processAsteroidEvent(Asteroid asteroid, Dimension attemptedPosition) throws InterruptedException {

        /*
        int attemptedX = attemptedPosition.width;
        int attemptedY = attemptedPosition.height;

        // Register how the asteroid wants to interact with each room
        Map<Room, EventType> interactionList = new HashMap<>();

        for (Room room : rectangleRoomList) {

            boolean attemptedPositionIsInRoom = ((attemptedX >= room.getPosition().width && attemptedX <= (room.getPosition().width + room.getSize().width))
                    && (attemptedY >= room.getPosition().height && attemptedY <= (room.getPosition().height + room.getSize().height)));

            if (!attemptedPositionIsInRoom && room.getAsteroidInside() != asteroid) {
                //If the asteroid is moving without interacting with the room
                interactionList.put(room, EventType.MOVE_ASTEROID);

            } else if ((!attemptedPositionIsInRoom) && room.getAsteroidInside() == asteroid) {
                //If the asteroid is in the room and is attempting to exit from it
                interactionList.put(room, EventType.ASTEROID_EXITS_ROOM);

            } else if (attemptedPositionIsInRoom && room.getAsteroidInside() == asteroid) {
                //If the asteroid is moving inside the room
                interactionList.put(room, EventType.ASTEROID_MOVES_INSIDE_ROOM);

            } else if (attemptedPositionIsInRoom && room.getAsteroidInside() != asteroid) {
                //If the asteroid is trying to enter the room
                if (room.getIsOccupied()) {
                    //If a asteroid is already in the room
                    interactionList.put(room, EventType.ASTEROID_ENTERS_OCCUPIED_ROOM);

                } else {
                    //If the room is free so the asteroid may enter
                    interactionList.put(room, EventType.ASTEROID_ENTERS_FREE_ROOM);
                }

            }
        }

         */

        boolean validMovement = true;
        /*
        for (Room room : rectangleRoomList) {
            if (interactionList.get(room).equals(EventType.ASTEROID_ENTERS_OCCUPIED_ROOM)) {
                validMovement = false;
            }
        }

         */

        if (validMovement) {

            /*
            for (Map.Entry<Room, EventType> interaction : interactionList.entrySet()) {
                Room room = interaction.getKey();
                EventType event = interaction.getValue();

                controller.asteroidEventManager(event, room, asteroid);
            }

             */

            int diameter = asteroid.getDIAMETER();

            if (attemptedPosition.width - diameter <= 0) {
                controller.asteroidEventManager(EventType.WEST_LIMIT_REACHED, asteroid);

            } else if (attemptedPosition.width + diameter >= getViewerWidth()) {
                controller.asteroidEventManager(EventType.EAST_LIMIT_REACHED, asteroid);

            } else if (attemptedPosition.height - diameter <= 0) {
                controller.asteroidEventManager(EventType.NORTH_LIMIT_REACHED, asteroid);

            } else if (attemptedPosition.height + diameter >= getViewerHeight()) {
                controller.asteroidEventManager(EventType.SOUTH_LIMIT_REACHED, asteroid);
            }

            asteroid.setPosition(attemptedPosition);
        }
    }

    // ---------- MAP BORDERS INTERACTION

    public void northLimitBounce(Asteroid asteroid) {
        Dimension speed = asteroid.getSpeed();
        speed.setSize(new Dimension(speed.width, abs(speed.height)));
    }

    public void southLimitBounce(Asteroid asteroid) {
        Dimension speed = asteroid.getSpeed();
        speed.setSize(new Dimension(speed.width, -abs(speed.height)));
    }

    public void eastLimitBounce(Asteroid asteroid) {
        Dimension speed = asteroid.getSpeed();
        speed.setSize(new Dimension(-abs(speed.width), speed.height));
    }

    public void westLimitBounce(Asteroid asteroid) {
        Dimension speed = asteroid.getSpeed();
        speed.setSize(new Dimension(abs(speed.width), speed.height));
    }

    // ---------- ASTEROID - ROOM INTERACTIONS

    /*
    public void asteroidEntersOccupiedRoom(Asteroid asteroid, Room room) {
        synchronized (room) {
            try {
                while (room.getIsOccupied()) {
                    room.wait();
                }
                room.setAsteroidInside(asteroid);
                room.setIsOccupied(true);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    synchronized public void asteroidEntersFreeRoom(Asteroid asteroid, Room room) {
        room.setAsteroidInside(asteroid);
        room.setIsOccupied(true);
    }

    public void asteroidMovesInsideRoom(Asteroid asteroid, Room room) {

    }

    public void asteroidExitsRoom(Asteroid asteroid, Room room) {
        synchronized (room) {
            room.setAsteroidInside(null);
            room.setIsOccupied(false);
            room.notifyAll();
        }
    }

     */


}
