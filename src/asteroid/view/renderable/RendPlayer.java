package asteroid.view.renderable;

import asteroid.controller.entity.EntityType;
import asteroid.dto.ShipMovementDto;
import helpers.ss_animation.Sprite;
import helpers.ss_animation.SpriteSheet;

import static java.lang.Thread.sleep;

public class RendPlayer extends RenderableSS {
    protected long lastTimeMoved;

    public RendPlayer(long entityId, EntityType type, Sprite sprite) {
        super(entityId, type, (SpriteSheet) sprite);
        this.lastTimeMoved = System.currentTimeMillis();
    }

    @Override
    public void updateSpriteIndexToNext() {
        if (currentSpriteIndex != maxCurrentSpriteIndex) currentSpriteIndex++;
    }

    public void updateSpriteIndexToPrevious() {
        if (currentSpriteIndex != 0) setCurrentSpriteIndex(0);
    }

    @Override
    public void run() {
        while (running) {
            ShipMovementDto smDto = this.viewer.getShipMovementDtoOfPlayer(this.entityId);
            if (smDto.movingUp || smDto.movingDown || smDto.movingLeft || smDto.movingRight) {
                long now = System.currentTimeMillis();
                if (now - lastTimeMoved < 200) updateSpriteIndexToNext();
                lastTimeMoved = now;
                updateSpriteIndexToNext();
            } else {
                updateSpriteIndexToPrevious();
            }

            try {
                sleep(this.spriteSheet.getDelay());
            } catch (InterruptedException ignored) {}
        }
    }

}
