package asteroid.model.body;

import asteroid.controller.entity.EntitySize;
import asteroid.controller.entity.EntityType;
import asteroid.dto.PhysicValuesDto;
import asteroid.physics.PhysicsEngine;
import asteroid.physics.ScalarPhysicalVariable;
import asteroid.physics.VectorialPhysicalVariable;
import java.awt.geom.Point2D;
import java.util.HashMap;

public abstract class MovingBody extends Body implements Runnable {
    protected Thread thread;
    protected volatile boolean running;

    public MovingBody(long entityId, EntityType type, EntitySize size, PhysicsEngine physicsEngine) {
        super(entityId, type, size, physicsEngine);
    }

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

    // =================================== THREAD ===================================

    public void startThread(){
        running = true;
        thread = new Thread(this);
        thread.start();
    }

    public void stopThread(){
        running = false;
        if (thread!=null){
            thread.interrupt();
        }
    }

    // =================================== LINKING METHODS ===================================

    public Point2D.Double getPosition() {
        return physicsEngine.getVectorialPhysicalValue(VectorialPhysicalVariable.POSITION);
    }

    public void setPosition(Point2D.Double position) {
        physicsEngine.setVectorialPhysicalValue(VectorialPhysicalVariable.POSITION, position);
    }

    public Point2D.Double getSpeed() {
        return physicsEngine.getVectorialPhysicalValue(VectorialPhysicalVariable.SPEED);
    }

}
