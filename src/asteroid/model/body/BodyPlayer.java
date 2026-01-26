package asteroid.model.body;

import asteroid.controller.entity.EntitySize;
import asteroid.controller.entity.EntityType;
import asteroid.dto.PhysicValuesDto;
import asteroid.dto.ShipMovementDto;
import asteroid.physics.ScalarPhysicalVariable;
import asteroid.physics.ShipPhysicsEngine;
import asteroid.physics.VectorialPhysicalVariable;
import helpers.CardinalDirection;

import java.awt.geom.Point2D;

import static java.lang.Thread.sleep;

public class BodyPlayer extends MovingBody {
    protected PlayerWeapon weapon;

    public BodyPlayer(long entityId, EntityType type, EntitySize size, ShipPhysicsEngine physicsEngine) {
        super(entityId, type, size, physicsEngine);
        this.size = size;
        this.rotationAngle = 0;
        this.weapon = new PlayerWeapon(this);
    }

    @Override
    public void run() {
        while (running) {
            PhysicValuesDto nextPhyValsDto = getNextPhysicalValues();

            this.physicsEngine.setVectorialPhysicalValuesSet(nextPhyValsDto.vectorialPhyVals);
            this.physicsEngine.setScalarPhysicalValuesSet(nextPhyValsDto.scalarPhyVals);

            try {
                sleep(30);
            } catch (InterruptedException ignored) {}
        }
    }

    @Override
    public void startThread(){
        running = true;
        thread = new Thread(this);
        thread.start();
        weapon.startThread();
    }

    @Override
    public void stopThread(){
        running = false;
        if (thread!=null){
            thread.interrupt();
        }
        weapon.stopThread();
    }

    // ----------------------------------- MOVEMENT -----------------------------------

    public void setMoving(boolean b, CardinalDirection direction) {
        ((ShipPhysicsEngine) this.physicsEngine).setMoving(b, direction);
    }

    public void calcRotationAngle(Point2D.Double mouseP) {
        Point2D shipPos = physicsEngine.getVectorialPhysicalValue(VectorialPhysicalVariable.POSITION);

        double dx = mouseP.x - shipPos.getX();
        double dy = mouseP.y - shipPos.getY();
        double finalRotation = Math.atan2(dy, dx);

        physicsEngine.setScalarPhysicalValue(ScalarPhysicalVariable.ROTATION_ANGLE, finalRotation);
    }

    public void setShooting(boolean b) {
        weapon.setShooting(b);
    }

    public void shot(int bulletSpeed) {
        model.generatePlayerShot(this.entityId,
                this.physicsEngine.getVectorialPhysicalValue(VectorialPhysicalVariable.POSITION),
                this.physicsEngine.getScalarPhysicalValue(ScalarPhysicalVariable.ROTATION_ANGLE),
                bulletSpeed);
    }

    public ShipMovementDto getShipMovementDto() {
        if (physicsEngine instanceof ShipPhysicsEngine) {
            return ((ShipPhysicsEngine) this.physicsEngine).getShipMovementDto(this.rotationAngle);
        }
        return null;
    }
}
