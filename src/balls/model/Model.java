package balls.model;

import balls.controller.Controller;
import balls.dto.Position;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class Model {
    private final Controller controller;
    private CopyOnWriteArrayList<Ball> ballList;
    private ArrayList<Room> rectangleRoomList;
    private volatile boolean paused=false;

    public Model(Controller controller) {
        this.controller = controller;
        this.ballList = new CopyOnWriteArrayList<Ball>();
        this.rectangleRoomList = new ArrayList<>();
    }
    public void addBall() {
        Ball ball = new Ball(this);
        ball.startThread();
        ballList.add(ball);
    }
    public void stopAllBalls(){
        for (Ball b : getAllBalls()){
            b.stopThread();
        }
    }
    public boolean isPaused(){
        return paused;
    }
    public void setPaused(boolean paused){
        this.paused=paused;
    }
    public void addRoom(Position position, Dimension size) {
        Room room = new Room(this, position, size);
        rectangleRoomList.add(room);
    }
    public CopyOnWriteArrayList<Ball> getAllBalls() {
        return ballList;
    }
    public int getViewerWidth() {return controller.getViewerWidth();}
    public int getViewerHeight() {return controller.getViewerHeight();}
    public ArrayList<Room> getAllRooms() {
        return this.rectangleRoomList;
    }

    /**
     * @param ball is the ball that calls the method for moving
     * @param attemptedPosition is the position the ball who called the function is willing to take
     * @return true -> the ball is allowed to move; false -> the movement got denied;
     */
    boolean collideDetection(Ball ball, Position attemptedPosition) throws InterruptedException {

        int attemptedX = attemptedPosition.width;
        int attemptedY = attemptedPosition.height;

        Map<Room, Boolean> interactionList = new HashMap<>();

        for (Room room : rectangleRoomList) {

            boolean attemptedPositionIsInRoom = ((attemptedX >= room.getPosition().width && attemptedX <= (room.getPosition().width + room.getSize().width))
                    && (attemptedY >= room.getPosition().height && attemptedY <= (room.getPosition().height + room.getSize().height)));

            if (!attemptedPositionIsInRoom && room.getBallInside() != ball) {
                //If the ball is moving without interacting with the room
                interactionList.put(room, true);

            } else if ((!attemptedPositionIsInRoom) && room.getBallInside() == ball) {
                // If the ball is in a room and is attempting to exit from it
                interactionList.put(room, room.exitingBall());

            } else if (attemptedPositionIsInRoom && room.getBallInside() == ball) {
                // If the ball is moving inside the room
                interactionList.put(room, true);

            } else {
                //If the ball is entering a room
                interactionList.put(room, room.enteringBall(ball));
            }
        }

        boolean validMovement = true;
        for (Room room : rectangleRoomList) {
            if (!interactionList.get(room)) {
                validMovement = false;
            }
        }

        return validMovement;
    }


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
}
