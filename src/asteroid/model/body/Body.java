package asteroid.model.body;

import asteroid.controller.entity.EntitySize;
import asteroid.controller.entity.EntityType;
import asteroid.dto.PhysicValuesDto;
import asteroid.model.Model;
import asteroid.physics.PhysicsEngine;
import asteroid.physics.ScalarPhysicalVariable;
import asteroid.physics.VectorialPhysicalVariable;

import java.awt.geom.Point2D;

public abstract class Body {
    protected Model model;
    protected final long entityId;
    protected EntityType type;
    protected PhysicsEngine physicsEngine;
    protected EntitySize size;
    protected double rotationAngle;

    public Body(long entityId, EntityType type, EntitySize size, PhysicsEngine physicsEngine) {
        this.entityId = entityId;
        this.size = size;
        this.type = type;
        this.physicsEngine = physicsEngine;
    }

    // ================================= GETTERS & SETTERS =================================

    public long getEntityId() {
        return this.entityId;
    }

    public EntityType getType() {
        return this.type;
    }

    public EntitySize getSize() {
        return this.size;
    }

    public void setSize(EntitySize size) {
        this.size = size;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    // ================================= LINKING METHODS =================================

    public Point2D.Double getVectorialPhysicalValue(VectorialPhysicalVariable key) {
        return this.physicsEngine.getVectorialPhysicalValue(key);
    }

    public Double getScalarPhysicalValue(ScalarPhysicalVariable key) {
        return this.physicsEngine.getScalarPhysicalValue(key);
    }

    public PhysicValuesDto getNextPhysicalValues() {
        return this.physicsEngine.calculateNextPhysicalValues();
    }


}
