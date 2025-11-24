package balls.model;

import balls.physics.BasicPhysicsEngine;
import balls.physics.PhysicsEngine;
import balls.dto.Position;

import java.awt.*;

import static java.lang.Math.abs;
import static java.lang.Thread.sleep;

public class Ball implements Runnable {
    private Model model;
    private final int DIAMETER;
    private final Color COLOR;
    private volatile boolean running=true;
    private Thread thread;
    private PhysicsEngine physicsEngine;

    public Ball(Model model) {
        this.model = model;
        this.DIAMETER = calcRandomDiameterBetweenValues(model.getMinBallSizeSliderValue(), model.getMaxBallSizeSliderValue());
        this.COLOR = generateRandomColor();

        this.physicsEngine = new BasicPhysicsEngine();
        physicsEngine.setPosition(calcStarterRandomPosition());
        physicsEngine.setSpeed(calcRandomSpeedBetweenValues(model.getMinBallSpeedSliderValue(), model.getMaxBallSpeedSliderValue()));
    }

    public Color getCOLOR() {
        return this.COLOR;
    }

    public Dimension getPosition() {
        return physicsEngine.getPosition();

    }

    public void setPosition(Dimension position) {
        physicsEngine.setPosition(position);
    }

    public void setSpeed(Dimension speed) {
        physicsEngine.setSpeed(speed);
    }

    public Dimension getSpeed() {
        return physicsEngine.getSpeed();
    }

    public int getDIAMETER() {return this.DIAMETER;}

    public void startThread(){
        running=true;
        thread=new Thread(this);
        thread.start();
    }
    public void stopThread(){
        running=false;
        if (thread!=null){
            thread.interrupt();
        }
    }

    @Override
    public void run() {
        while (running) {
            try {
                while (model.getIsPaused() && running) {
                    Thread.sleep(50);
                }

                Dimension[] nextPhysicalValues = physicsEngine.calculateNextPhysicalValues();
                Dimension attemptedPosition = nextPhysicalValues[0];
                model.processBallEvent(this, attemptedPosition);
                Thread.sleep(10);

            }catch (InterruptedException e){
                running=false;
                Thread.currentThread().interrupt();
            }
        }
    }
    private Position calcStarterRandomPosition() {
        Position starterRandomPosition = new Position(0,0);
        boolean validStarterPosition = false;

        while (!validStarterPosition) {
            starterRandomPosition = new Position((int) (Math.random() * model.getViewerWidth()), (int) (Math.random() * model.getViewerHeight()));

            for (Room room : model.getAllRooms()) {

                Position roomPosition = room.getPosition();
                Dimension roomSize = room.getSize();
                boolean validWidth = false;
                boolean validHeight = false;

                if (!(starterRandomPosition.width > roomPosition.width && starterRandomPosition.width < (roomPosition.width + roomSize.width))) {
                    validWidth = true;
                }
                if (!(starterRandomPosition.height > roomPosition.height && starterRandomPosition.height < (roomPosition.height + roomSize.height))) {
                    validHeight = true;
                }
                validStarterPosition=(validWidth && validHeight);
            }
        }
        return starterRandomPosition;
    }
    private Position calcNewPosition(Position position, Dimension speed) {
        int attemptedX = position.width + speed.width;
        int attemptedY = position.height + speed.height;
        return new Position(attemptedX, attemptedY);
    }
    private Dimension calcRandomSpeedBetweenValues(int minSpeed, int maxSpeed) {
        int randomXSpeed = (int) (Math.random() * ((maxSpeed - minSpeed) + 1)) + minSpeed;
        int randomYSpeed = (int) (Math.random() * ((maxSpeed - minSpeed) + 1)) + minSpeed;
        return new Dimension(randomXSpeed, randomYSpeed);
    }
    private int calcRandomDiameterBetweenValues(int minDiameter, int maxDiameter) {
        return (int) (Math.random() * ((maxDiameter - minDiameter) + 1)) + minDiameter;
    }
    private Color generateRandomColor() {
        int redValue = (int)(Math.random() * 256);
        int greenValue = (int)(Math.random() * 256);
        int blueValue = (int)(Math.random() * 256);
        return new Color(redValue, greenValue, blueValue);
    }
}
