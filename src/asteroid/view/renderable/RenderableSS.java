package asteroid.view.renderable;

import asteroid.controller.entity.EntityType;
import helpers.ss_animation.SpriteSheet;

import java.awt.*;

/**
 * A Renderable class who implements SpriteSheets for animating its sprite
 */
public abstract class RenderableSS extends Renderable implements Runnable {
    protected Thread thread;
    protected volatile boolean running;
    protected SpriteSheet spriteSheet;
    protected final int maxCurrentSpriteIndex;
    protected int currentSpriteIndex;

    public RenderableSS(long entityId, EntityType type, SpriteSheet spriteSheet) {
        super(entityId, type, spriteSheet);
        this.maxCurrentSpriteIndex = spriteSheet.getMaxFrameIndex();
        this.currentSpriteIndex = 0;
        this.spriteSheet = spriteSheet;
    }

    @Override
    public Image getImage() {
        return spriteSheet.getFrameAt(currentSpriteIndex);
    }

    synchronized public void setCurrentSpriteIndex(int index) {
        this.currentSpriteIndex = index;
    }

    public void updateSpriteIndexToNext() {
        if (currentSpriteIndex == maxCurrentSpriteIndex) {
            setCurrentSpriteIndex(0);
        } else {
            currentSpriteIndex++;
        }
    }

    public void startThread(){
        running = true;
        thread = new Thread(this);
        thread.start();
    }

    public void stopThread(){
        running = false;
        if (thread!=null){
            thread.interrupt();
        }
    }

    @Override
    public void run() {

    }

}
