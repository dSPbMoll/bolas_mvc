package asteroid.model.body;

import asteroid.controller.entity.EntityType;
import asteroid.dto.PhysicValuesDto;
import asteroid.model.Model;
import asteroid.model.physics.PhysicsEngine;
import asteroid.model.physics.ScalarPhysicalVariable;
import asteroid.model.physics.VectorialPhysicalVariable;

import java.awt.geom.Point2D;

public abstract class Body {
    protected Model model;
    protected final long entityId;
    protected EntityType type;
    protected PhysicsEngine physicsEngine;
    protected Point2D.Double size;
    protected double rotationAngle;

    public Body(long entityId, EntityType type, Point2D.Double size, PhysicsEngine physicsEngine) {
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

    public Point2D.Double getSize() {
        return this.size;
    }

    public void setSize(Point2D.Double size) {
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
