package asteroid.controller.entity;

public abstract class EntitySize {
    protected final EntitySizeType type;

    public EntitySize(EntitySizeType type) {
        this.type = type;
    }

    public EntitySizeType getType() {
        return this.type;
    }
}
