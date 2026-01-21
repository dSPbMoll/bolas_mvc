package asteroid.view.renderable;

import asteroid.controller.entity.EntityType;
import helpers.ss_animation.SpriteSheet;

import static java.lang.Thread.sleep;

public class SelfUpdatingRenderableSS extends RenderableSS {

    public SelfUpdatingRenderableSS(long entityId, EntityType type, SpriteSheet spriteSheet) {
        super(entityId, type, spriteSheet);
    }

    @Override
    public void run() {
        while (running) {
            updateSpriteIndexToNext();
            try {
                sleep(this.spriteSheet.getDelay());
            } catch (InterruptedException ignored) {}
        }
    }

}
