package helpers.ss_animation;

import java.awt.*;

public class SpriteSheet extends Sprite {
    private final Image sheet;
    private int delay;
    private final Image[] frames;

    public SpriteSheet(Image sheet, Image[] frames, int delay) {
        super(frames[0]);
        this.sheet = sheet;
        this.frames = frames;
        this.delay = delay;
        this.sprite = getFrameAt(0);
    }

    // ---------------------------------------------- GETTERS & SETTERS ----------------------------------------------

    public int getDelay() {
        return this.delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public Image getFrameAt(int index) {
        return frames[index];
    }

    public int getMaxFrameIndex() {
        return frames.length -1;
    }
}
