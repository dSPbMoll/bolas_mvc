package asteroid.model.body;

import asteroid.controller.entity.EntitySize;
import asteroid.controller.entity.EntityType;
import asteroid.dto.PhysicValuesDto;
import asteroid.physics.PhysicsEngine;
import asteroid.physics.PhysicsEngineInterface;
import asteroid.physics.ScalarPhysicalVariable;
import asteroid.physics.VectorialPhysicalVariable;

import java.awt.geom.Point2D;
import java.util.HashMap;

import static java.lang.Math.abs;
import static java.lang.Thread.sleep;

public class BodyAsteroid extends MovingBody {
    protected PhysicsEngineInterface physicsEngine;

    public BodyAsteroid(long entityId, EntityType type, EntitySize size, PhysicsEngine physicsEngine) {
        super(entityId, type, size, physicsEngine);

        this.physicsEngine = physicsEngine;
    }

    // ================================== THREAD ==================================

    @Override
    public void run() {
        while (running) {
            PhysicValuesDto phyValsDto = physicsEngine.calculateNextPhysicalValues();
            HashMap<VectorialPhysicalVariable, Point2D.Double> vectPhyVals = phyValsDto.vectorialPhyVals;
            HashMap<ScalarPhysicalVariable, Double> scalarPhyVals = phyValsDto.scalarPhyVals;

            Point2D.Double attemptedPosition = vectPhyVals.get(VectorialPhysicalVariable.POSITION);
            model.processMovingBodyEvent(this, attemptedPosition);

            //TODO implement a way to update all the physical variables if the movement is allowed by the model

            try {
                Thread.sleep(10);
            } catch (InterruptedException ignored) {}
        }
    }

    // -------------------------------- GETTERS & SETTERS --------------------------------

}
