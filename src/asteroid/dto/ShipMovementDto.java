package asteroid.dto;

public class ShipMovementDto {
    public final boolean movingUp;
    public final boolean movingDown;
    public final boolean movingLeft;
    public final boolean movingRight;
    public final double rotationAngle;

    public ShipMovementDto(boolean movingUp, boolean movingDown, boolean movingLeft, boolean movingRight, double rotationAngle) {
        this.movingUp = movingUp;
        this.movingDown = movingDown;
        this.movingLeft = movingLeft;
        this.movingRight = movingRight;
        this.rotationAngle = rotationAngle;
    }
}
