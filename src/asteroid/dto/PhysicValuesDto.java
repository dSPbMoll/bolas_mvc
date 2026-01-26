package asteroid.dto;

import asteroid.physics.ScalarPhysicalVariable;
import asteroid.physics.VectorialPhysicalVariable;

import java.awt.geom.Point2D;
import java.util.HashMap;

public class PhysicValuesDto {
    public final HashMap<VectorialPhysicalVariable, Point2D.Double> vectorialPhyVals;
    public HashMap<ScalarPhysicalVariable, Double> scalarPhyVals;

    public PhysicValuesDto(HashMap<VectorialPhysicalVariable, Point2D.Double> vectorialPhyVals) {
        this.vectorialPhyVals = vectorialPhyVals;
    }

    public PhysicValuesDto(HashMap<VectorialPhysicalVariable, Point2D.Double> vectorialPhyVals,
                           HashMap<ScalarPhysicalVariable, Double> scalarPhyVals) {
        this.vectorialPhyVals = vectorialPhyVals;
        this.scalarPhyVals = scalarPhyVals;
    }

}
