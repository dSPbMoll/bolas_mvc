package helpers.ss_animation;

import java.awt.*;

public class Sprite {
    protected Image sprite;

    public Sprite(Image sprite) {
        this.sprite = sprite;
    }

    // ---------------------------------------------- GETTERS & SETTERS ----------------------------------------------

    public Image getSprite() {
        return this.sprite;
    }
}
