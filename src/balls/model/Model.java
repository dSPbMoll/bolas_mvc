package balls.model;

import balls.controller.Controller;
import balls.dto.Position;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import static java.lang.Math.abs;

public class Model {
    private final Controller controller;
    private ArrayList<Ball> ballList;
    //private ArrayList<Room> rectangleRoomList;
    private volatile boolean isPaused = false;
    private Player player;

    public Model(Controller controller) {
        this.controller = controller;
        this.ballList = new ArrayList<>();
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

    synchronized public ArrayList<Ball> getAllBalls() {
        return ballList;
    }

    // ------------------------------------ LINKING METHODS ------------------------------------

    public int getViewerWidth() {return controller.getViewerWidth();}

    public int getViewerHeight() {return controller.getViewerHeight();}

    public int getMinBallSpeedSliderValue() {
        return controller.getMinBallSpeedSliderValue();
    }

    public int getMaxBallSpeedSliderValue() {
        return controller.getMaxBallSpeedSliderValue();
    }

    public int getMinBallSizeSliderValue() {
        return controller.getMinBallSizeSliderValue();
    }

    public int getMaxBallSizeSliderValue() {
        return controller.getMaxBallSizeSliderValue();
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

    synchronized public void addBall() {
        Ball ball = new Ball(this);
        ball.startThread();
        ballList.add(ball);
    }

    public void stopAllBalls(){
        for (Ball b : ballList){
            b.stopThread();
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
     * @param ball is the ball that calls the method for moving
     * @param attemptedPosition is the position the ball who called the function is willing to take
     * @return true -> the ball is allowed to move; false -> the movement got denied;
     */
    synchronized public void processBallEvent(Ball ball, Dimension attemptedPosition) throws InterruptedException {

        /*
        int attemptedX = attemptedPosition.width;
        int attemptedY = attemptedPosition.height;

        // Register how the ball wants to interact with each room
        Map<Room, EventType> interactionList = new HashMap<>();

        for (Room room : rectangleRoomList) {

            boolean attemptedPositionIsInRoom = ((attemptedX >= room.getPosition().width && attemptedX <= (room.getPosition().width + room.getSize().width))
                    && (attemptedY >= room.getPosition().height && attemptedY <= (room.getPosition().height + room.getSize().height)));

            if (!attemptedPositionIsInRoom && room.getBallInside() != ball) {
                //If the ball is moving without interacting with the room
                interactionList.put(room, EventType.MOVE_BALL);

            } else if ((!attemptedPositionIsInRoom) && room.getBallInside() == ball) {
                //If the ball is in the room and is attempting to exit from it
                interactionList.put(room, EventType.BALL_EXITS_ROOM);

            } else if (attemptedPositionIsInRoom && room.getBallInside() == ball) {
                //If the ball is moving inside the room
                interactionList.put(room, EventType.BALL_MOVES_INSIDE_ROOM);

            } else if (attemptedPositionIsInRoom && room.getBallInside() != ball) {
                //If the ball is trying to enter the room
                if (room.getIsOccupied()) {
                    //If a ball is already in the room
                    interactionList.put(room, EventType.BALL_ENTERS_OCCUPIED_ROOM);

                } else {
                    //If the room is free so the ball may enter
                    interactionList.put(room, EventType.BALL_ENTERS_FREE_ROOM);
                }

            }
        }

         */

        boolean validMovement = true;
        /*
        for (Room room : rectangleRoomList) {
            if (interactionList.get(room).equals(EventType.BALL_ENTERS_OCCUPIED_ROOM)) {
                validMovement = false;
            }
        }

         */

        if (validMovement) {

            /*
            for (Map.Entry<Room, EventType> interaction : interactionList.entrySet()) {
                Room room = interaction.getKey();
                EventType event = interaction.getValue();

                controller.ballEventManager(event, room, ball);
            }

             */

            int diameter = ball.getDIAMETER();

            if (attemptedPosition.width - diameter <= 0) {
                controller.ballEventManager(EventType.WEST_LIMIT_REACHED, ball);

            } else if (attemptedPosition.width + diameter >= getViewerWidth()) {
                controller.ballEventManager(EventType.EAST_LIMIT_REACHED, ball);

            } else if (attemptedPosition.height - diameter <= 0) {
                controller.ballEventManager(EventType.NORTH_LIMIT_REACHED, ball);

            } else if (attemptedPosition.height + diameter >= getViewerHeight()) {
                controller.ballEventManager(EventType.SOUTH_LIMIT_REACHED, ball);
            }

            ball.setPosition(attemptedPosition);
        }
    }

    // ---------- MAP BORDERS INTERACTION

    public void northLimitBounce(Ball ball) {
        Dimension speed = ball.getSpeed();
        speed.setSize(new Dimension(speed.width, abs(speed.height)));
    }

    public void southLimitBounce(Ball ball) {
        Dimension speed = ball.getSpeed();
        speed.setSize(new Dimension(speed.width, -abs(speed.height)));
    }

    public void eastLimitBounce(Ball ball) {
        Dimension speed = ball.getSpeed();
        speed.setSize(new Dimension(-abs(speed.width), speed.height));
    }

    public void westLimitBounce(Ball ball) {
        Dimension speed = ball.getSpeed();
        speed.setSize(new Dimension(abs(speed.width), speed.height));
    }

    // ---------- BALL - ROOM INTERACTIONS

    /*
    public void ballEntersOccupiedRoom(Ball ball, Room room) {
        synchronized (room) {
            try {
                while (room.getIsOccupied()) {
                    room.wait();
                }
                room.setBallInside(ball);
                room.setIsOccupied(true);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    synchronized public void ballEntersFreeRoom(Ball ball, Room room) {
        room.setBallInside(ball);
        room.setIsOccupied(true);
    }

    public void ballMovesInsideRoom(Ball ball, Room room) {

    }

    public void ballExitsRoom(Ball ball, Room room) {
        synchronized (room) {
            room.setBallInside(null);
            room.setIsOccupied(false);
            room.notifyAll();
        }
    }

     */


}
