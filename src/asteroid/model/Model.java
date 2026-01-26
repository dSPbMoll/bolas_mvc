package asteroid.model;

import asteroid.controller.Controller;
import asteroid.controller.entity.EntityType;
import asteroid.dto.BodyDto;
import asteroid.dto.ShipMovementDto;
import asteroid.model.body.*;
import asteroid.physics.ScalarPhysicalVariable;
import asteroid.physics.VectorialPhysicalVariable;
import config.simulation.WorldConfig;
import helpers.CardinalDirection;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import static java.lang.Math.abs;

public class Model {
    private final Controller controller;
    private final HashMap<EntityType, CopyOnWriteArrayList<Body>> bodies;
    private final HashMap<WorldConfig, Integer> worldConfigs;

    public Model(Controller controller, HashMap<WorldConfig, Integer> worldConfigs) {
        this.controller = controller;
        this.worldConfigs = worldConfigs;

        bodies = new HashMap<>();
        bodies.put(EntityType.ASTEROID, new CopyOnWriteArrayList<>());
        bodies.put(EntityType.PLAYER, new CopyOnWriteArrayList<>());
        bodies.put(EntityType.PLANET, new CopyOnWriteArrayList<>());
        bodies.put(EntityType.SHOT, new CopyOnWriteArrayList<>());
    }

    // ------------------------------------ GETTERS & SETTERS ------------------------------------

    public ArrayList<BodyDto> getAllBodyDtosByType(EntityType type) {
        return turnBodiesIntoDtos(type);
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

    private Body searchBody(EntityType type, long entityId) {
        for (Body body : bodies.get(EntityType.PLAYER)) {
            if (entityId == body.getEntityId()) return body;
        }
        return null;
    }

    public void setPlayerMoving(int entityId, boolean b, CardinalDirection direction) {
        Body body = searchBody(EntityType.PLAYER, entityId);
        if (body instanceof BodyPlayer) ((BodyPlayer)body).setMoving(b, direction);
    }

    public void calcRotationAngleOfPlayer(Point2D.Double mouseP, int entityId) {
        Body body = searchBody(EntityType.PLAYER, entityId);
        if (body instanceof BodyPlayer) ((BodyPlayer) body).calcRotationAngle(mouseP);
    }

    public void setPlayerShooting(long entityId, boolean b) {
        Body body = searchBody(EntityType.PLAYER, entityId);
        if (body instanceof BodyPlayer) ((BodyPlayer) body).setShooting(b);
    }

    public void generatePlayerShot(long entityId, Point2D.Double position, double rotationAngle, int bulletSpeed) {
        controller.generatePlayerShot(entityId, position, rotationAngle, bulletSpeed);
    }

    public ShipMovementDto getShipMovementDtoOfPlayer(long entityId) {
        Body body = searchBody(EntityType.PLAYER, entityId);
        if (body instanceof BodyPlayer) return ((BodyPlayer) body).getShipMovementDto();
        return null;
    }

    // ==================================== DTO BUILDING ====================================

    private ArrayList<BodyDto> turnBodiesIntoDtos(EntityType type) {
        ArrayList<BodyDto> bodyDtos = new ArrayList<>();
        for (Body body : bodies.get(type)) {
            bodyDtos.add(new BodyDto(
                    body.getType(),
                    body.getEntityId(),
                    body.getVectorialPhysicalValue(VectorialPhysicalVariable.POSITION),
                    body.getSize(),
                    body.getScalarPhysicalValue(ScalarPhysicalVariable.ROTATION_ANGLE)
            ));
        }
        return bodyDtos;
    }

    // ------------------------------------ SIMULATION MODIFYING METHODS ------------------------------------

    public void startAllMovingBodies(){
        for (Map.Entry<EntityType, CopyOnWriteArrayList<Body>> entry : this.bodies.entrySet()) {
            for (Body body : entry.getValue()) {
                if (body instanceof MovingBody) {
                    ((MovingBody) body).startThread();
                }
            }
        }
    }

    public void stopAllMovingBodies(){
        for (Map.Entry<EntityType, CopyOnWriteArrayList<Body>> entry : this.bodies.entrySet()) {
            for (Body body : entry.getValue()) {
                if (body instanceof MovingBody) {
                    ((MovingBody) body).stopThread();
                }
            }
        }
    }

    public void deleteAllBodies() {
        for (Map.Entry<EntityType, CopyOnWriteArrayList<Body>> entry : bodies.entrySet()) {
            entry.getValue().clear();
        }
    }

    public void deleteBody(EntityType type, long entityId) {
        CopyOnWriteArrayList<Body> iteratedList = bodies.get(type);

        for (Body body : iteratedList) {
            if (body.getEntityId() == entityId) {
                if (body instanceof MovingBody) ((MovingBody) body).stopThread();
                iteratedList.remove(body);
            }
        }
    }

    public void addBody(Body body) {
        body.setModel(this);
        boolean added = false;

        switch (body.getType()) {
            case ASTEROID:
                bodies.get(EntityType.ASTEROID).add(body);
                if (body instanceof BodyAsteroid) ((BodyAsteroid) body).startThread();
                added = true;
                break;

            case PLAYER:
                bodies.get(EntityType.PLAYER).add(body);
                if (body instanceof BodyPlayer) ((BodyPlayer) body).startThread();
                added = true;
                break;

            case PLANET:
                bodies.get(EntityType.PLANET).add(body);
                added = true;
                break;

            case SHOT:
                bodies.get(EntityType.SHOT).add(body);
                if (body instanceof BodyShot) ((BodyShot) body).startThread();
                added = true;
                break;
        }
        if (!added) throw new NullPointerException("Could not add entity to the Model's entities list");
    }

    // ------------------------------------ SIMULATION CONTROL METHODS ------------------------------------
    // ---------- GENERAL EVENT PROCESSING

    /**
     * @param movingBody is the asteroid that calls the method for moving
     * @param attemptedPosition is the position the asteroid who called the function is willing to take
     * @return true -> the asteroid is allowed to move; false -> the movement got denied;
     */
    synchronized public void processMovingBodyEvent(MovingBody movingBody, Point2D.Double attemptedPosition) {

        boolean validMovement = true;

        if (validMovement) {
            /*
            int diameter = 0;

            EntitySize size = movingBody.getSize();
            if (size instanceof CircularSize) diameter = ((CircularSize) size).getDiameter();
            if (size instanceof RectangularSize) diameter = (int)((RectangularSize) size).getValues().getX();

             */

            if (attemptedPosition.x <= 0) {
                controller.movingBodyEventManager(EventType.WEST_LIMIT_REACHED, movingBody);

            } else if (attemptedPosition.x >= worldConfigs.get(WorldConfig.WIDTH)) {
                controller.movingBodyEventManager(EventType.EAST_LIMIT_REACHED, movingBody);

            } else if (attemptedPosition.y <= 0) {
                controller.movingBodyEventManager(EventType.NORTH_LIMIT_REACHED, movingBody);

            } else if (attemptedPosition.y >= worldConfigs.get(WorldConfig.HEIGHT)) {
                controller.movingBodyEventManager(EventType.SOUTH_LIMIT_REACHED, movingBody);
            }

            try {
                movingBody.setPosition(attemptedPosition);
            } catch (NullPointerException e) {
                System.out.println("Deleted entity " + movingBody.getEntityId() + " successfully");
            }

        }
    }

    // ---------- MAP BORDERS INTERACTION

    public void northLimitBounce(MovingBody movingBody) {
        Point2D.Double speed = movingBody.getSpeed();
        speed.setLocation(new Point2D.Double(speed.x, abs(speed.y)));
    }

    public void southLimitBounce(MovingBody movingBody) {
        Point2D.Double speed = movingBody.getSpeed();
        speed.setLocation(new Point2D.Double(speed.x, -abs(speed.y)));
    }

    public void eastLimitBounce(MovingBody movingBody) {
        Point2D.Double speed = movingBody.getSpeed();
        speed.setLocation(new Point2D.Double(-abs(speed.x), speed.y));
    }

    public void westLimitBounce(MovingBody movingBody) {
        Point2D.Double speed = movingBody.getSpeed();
        speed.setLocation(new Point2D.Double(abs(speed.x), speed.y));
    }

}
