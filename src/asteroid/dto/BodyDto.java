package asteroid.dto;

import asteroid.controller.entity.EntitySize;
import asteroid.controller.entity.EntityType;
import java.awt.geom.Point2D;

public class BodyDto {
    public final EntityType type;
    public final long entityId;
    public final Point2D.Double position;
    public final EntitySize size;
    public final double rotationAngle;

    public BodyDto(EntityType type, long entityId, Point2D.Double position, EntitySize size, double rotationAngle) {
        this.type = type;
        this.entityId = entityId;
        this.position = position;
        this.size = size;
        this.rotationAngle = rotationAngle;
    }
}
