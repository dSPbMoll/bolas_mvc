package asteroid.dto;

public class EntityParamsDto {
    public final int MIN_DIAMETER;
    public final int MAX_DIAMETER;
    public final int MIN_SPEED;
    public final int MAX_SPEED;

    public EntityParamsDto(int MIN_DIAMETER, int MAX_DIAMETER, int MIN_SPEED, int MAX_SPEED) {
        this.MIN_DIAMETER = MIN_DIAMETER;
        this.MAX_DIAMETER = MAX_DIAMETER;
        this.MIN_SPEED = MIN_SPEED;
        this.MAX_SPEED = MAX_SPEED;
    }
}
