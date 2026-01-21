package asteroid.controller.entity;

public class CircularSize extends EntitySize {
    protected final int diameter;

    public CircularSize(int diameter) {
        super(EntitySizeType.CIRCULAR);
        this.diameter = diameter;
    }

    public int getDiameter() {
        return this.diameter;
    }
}
