package asteroid.controller.entity;

import java.awt.geom.Point2D;

public class RectangularSize extends EntitySize {
    protected final Point2D.Double values;

    public RectangularSize(Point2D.Double values) {
        super(EntitySizeType.RECTANGULAR);
        this.values = values;
    }

    public Point2D.Double getValues() {
        return this.values;
    }
}
