package asteroid.model.body;

import asteroid.controller.entity.EntityType;
import asteroid.model.physics.PhysicsEngine;

import java.awt.geom.Point2D;

public class BodyShot extends MovingBody {
    protected final long entityWhoShot;

    public BodyShot(long entityId, long entityWhoShot, EntityType type, Point2D.Double size, PhysicsEngine physicsEngine) {
        super(entityId, type, size, physicsEngine);
        this.entityWhoShot = entityWhoShot;

    }
}
