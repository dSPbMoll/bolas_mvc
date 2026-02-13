package asteroid.model.body;

import asteroid.controller.entity.EntityType;
import asteroid.model.physics.PhysicsEngine;

import java.awt.geom.Point2D;

public class BodyPlanet extends Body {

    public BodyPlanet(int entityId, EntityType type, Point2D.Double size, PhysicsEngine physicsEngine) {
        super(entityId, type, size, physicsEngine);
    }
}
