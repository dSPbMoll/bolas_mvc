package communications.dto;

import asteroid.controller.entity.EntityType;

import java.awt.geom.Point2D;
import java.io.Serializable;

public class PayloadComms implements Serializable {
    public final EntityType type;
    public final Point2D.Double position;
    public final Point2D.Double speed;
    public final Point2D.Double size;
    public final double rotationAngle;
    public final int assetId;


    public PayloadComms(EntityType type, Point2D.Double position, Point2D.Double speed,
            Point2D.Double size, double rotationAngle, int assetId) {

        this.type = type;
        this.position = position;
        this.speed = speed;
        this.size = size;
        this.rotationAngle = rotationAngle;
        this.assetId = assetId;
    }
}
