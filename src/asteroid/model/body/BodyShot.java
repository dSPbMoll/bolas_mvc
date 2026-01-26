package asteroid.model.body;

import asteroid.controller.entity.EntitySize;
import asteroid.controller.entity.EntityType;
import asteroid.physics.PhysicsEngine;

public class BodyShot extends MovingBody {
    protected final long entityWhoShot;

    public BodyShot(long entityId, long entityWhoShot, EntityType type, EntitySize size, PhysicsEngine physicsEngine) {
        super(entityId, type, size, physicsEngine);
        this.entityWhoShot = entityWhoShot;

    }
}
