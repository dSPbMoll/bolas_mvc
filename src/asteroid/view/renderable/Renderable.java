package asteroid.view.renderable;

import asteroid.controller.entity.EntityType;
import asteroid.view.Viewer;
import helpers.ss_animation.Sprite;

import java.awt.*;

public abstract class Renderable {
    protected Viewer viewer;
    protected final long entityId;
    protected EntityType type;
    protected volatile Sprite sprite;

    public Renderable(long entityId, EntityType type, Sprite sprite) {
        this.entityId = entityId;
        this.type = type;
        this.sprite = sprite;
    }

    // ================================= GETTERS & SETTERS =================================

    public long getEntityId() {
        return this.entityId;
    }

    public EntityType getType() {
        return this.type;
    }

    public void setViewer(Viewer viewer) {
        this.viewer = viewer;
    }

    synchronized public Image getImage() {
        return this.sprite.getSprite();
    }
}
