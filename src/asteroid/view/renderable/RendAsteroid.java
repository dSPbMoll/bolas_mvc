package asteroid.view.renderable;

import asteroid.controller.entity.EntityType;
import helpers.ss_animation.Sprite;
import helpers.ss_animation.SpriteSheet;

public class RendAsteroid extends SelfUpdatingRenderableSS {

    public RendAsteroid(long entityId, EntityType type, Sprite sprite) {
        super(entityId, type, (SpriteSheet) sprite);
    }
}
