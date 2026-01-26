package asteroid.controller;

import asteroid.controller.entity.EntityType;
import asteroid.dto.BodyDto;
import asteroid.dto.EntityParamsDto;
import asteroid.dto.ShipMovementDto;
import asteroid.life_generation.LifeGenerator;
import asteroid.model.body.Body;
import asteroid.model.EventType;
import asteroid.model.body.MovingBody;
import asteroid.view.View;
import asteroid.model.Model;
import asteroid.view.renderable.Renderable;
import config.player.ControlConfig;
import config.simulation.AsteroidConfig;
import config.simulation.WorldConfig;
import helpers.CardinalDirection;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class Controller {
    private final Model model;
    private final View view;
    private final HashMap<EntityType, CopyOnWriteArrayList<Long>> entities;
    private LifeGenerator lifeGenerator;

    public Controller(
            HashMap<ControlConfig, String> playerControlConfigs,
            HashMap<WorldConfig, Integer> worldConfigs,
            HashMap<AsteroidConfig, Integer> asteroidConfigs) {

        this.model = new Model(this, worldConfigs);
        this.view = new View(this, playerControlConfigs, worldConfigs);
        this.lifeGenerator = new LifeGenerator(this, worldConfigs, asteroidConfigs);
        this.entities = new HashMap<>();

        entities.put(EntityType.ASTEROID, new CopyOnWriteArrayList<>());
        entities.put(EntityType.PLAYER, new CopyOnWriteArrayList<>());
        entities.put(EntityType.PLANET, new CopyOnWriteArrayList<>());
        entities.put(EntityType.SHOT, new CopyOnWriteArrayList<>());

        lifeGenerator.generateRandomizedEntityOfType(EntityType.PLAYER);
    }

    // ------------------------------- MODEL EVENTS MANAGING -------------------------------

    public void addEntity(Body body, Renderable renderable) {
        if (body.getType() != renderable.getType()) throw new IllegalArgumentException("Body's and Renderable's types doesn't match");

        boolean addedToEntities = false;
        switch (body.getType()) {
            case EntityType.ASTEROID:
                entities.get(EntityType.ASTEROID).add(body.getEntityId());
                addedToEntities = true;
                break;

            case EntityType.PLAYER:
                entities.get(EntityType.PLAYER).add(body.getEntityId());
                addedToEntities = true;
                break;

            case EntityType.PLANET:
                entities.get(EntityType.PLANET).add(body.getEntityId());
                addedToEntities = true;
                break;

            case EntityType.SHOT:
                entities.get(EntityType.SHOT).add(body.getEntityId());
                addedToEntities = true;
                break;
        }
        if (!addedToEntities) throw new NullPointerException("Entity not present in Controller's entities list");

        model.addBody(body);
        view.addRenderable(renderable);
    }

    public void movingBodyEventManager(EventType event, MovingBody movingBody) {
        switch (event) {
            case NORTH_LIMIT_REACHED:
                deleteEntity(movingBody.getType(), movingBody.getEntityId());
                //model.northLimitBounce(movingBody);
                break;

            case SOUTH_LIMIT_REACHED:
                deleteEntity(movingBody.getType(), movingBody.getEntityId());
                //model.southLimitBounce(movingBody);
                break;

            case EAST_LIMIT_REACHED:
                deleteEntity(movingBody.getType(), movingBody.getEntityId());
                //model.eastLimitBounce(movingBody);
                break;

            case WEST_LIMIT_REACHED:
                deleteEntity(movingBody.getType(), movingBody.getEntityId());
                //model.westLimitBounce(movingBody);
                break;

        }
    }

    // ------------------------------------- GETTERS & SETTERS -------------------------------------

    // ------------------------------------- LINKING METHODS -------------------------------------

    public ArrayList<BodyDto> getAllBodyDtosByType(EntityType type) {
        return model.getAllBodyDtosByType(type);
    }

    public void stopAllMovingBodies(){
        model.stopAllMovingBodies();
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

    public void startAllMovingBodies() {
        model.startAllMovingBodies();
    }

    public void sendNewEntityParamsToLifeGenerator(EntityParamsDto params) {
        lifeGenerator.setEntityParams(params);
    }

    public void generateEntityInLifeGenerator() {
        lifeGenerator.generateRandomEntity();
    }

    public void setRunningLifeGenerator(boolean b){
        lifeGenerator.setRunning(b);
    }

    public void setPlayerShooting(long entityId, boolean b) {
        model.setPlayerShooting(entityId, b);
    }

    public void generatePlayerShot(long entityId, Point2D.Double position, double rotationAngle, int bulletSpeed) {
        lifeGenerator.generatePlayerShot(entityId, position, rotationAngle, bulletSpeed);
    }

    public void startLifeGenerator() {
        lifeGenerator.startThread();
    }

    public void stopLifeGenerator() {
        lifeGenerator.stopThread();
    }

    // ------------- PLAYER

    public void setPlayerMoving(int entityId, boolean b, CardinalDirection direction) {
        model.setPlayerMoving(entityId, b, direction);
    }

    public void calcRotationAngleOfPlayer(Point2D.Double mouseP, int entityId) {
        for (Long id : entities.get(EntityType.PLAYER)) {
            if (id == entityId) {
                model.calcRotationAngleOfPlayer(mouseP, 1);
            }
        }
    }

    public ShipMovementDto getShipMovementDtoOfPlayer(long entityId) {
        return model.getShipMovementDtoOfPlayer(entityId);
    }

    // --------- OTHER

    public int getViewerWidth() {
        return view.getViewerWidth();
    }

    public int getViewerHeight() {
        return view.getViewerHeight();
    }

    public void deleteAllEntities() {
        for (Map.Entry<EntityType, CopyOnWriteArrayList<Long>> entry : entities.entrySet()) {
            entry.getValue().clear();
        }
        view.deleteAllRenderables();
        model.deleteAllBodies();
    }

    public void deleteEntity(EntityType type, long entityId) {
        model.deleteBody(type, entityId);
        view.deleteRenderable(type, entityId);

        entities.get(type).remove(entityId);
    }


}
