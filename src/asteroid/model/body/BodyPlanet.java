package asteroid.model.body;

import asteroid.controller.entity.EntitySize;
import asteroid.controller.entity.EntityType;
import asteroid.physics.PhysicsEngine;

public class BodyPlanet extends Body {

    public BodyPlanet(int entityId, EntityType type, EntitySize size, PhysicsEngine physicsEngine) {
        super(entityId, type, size, physicsEngine);
    }
}
