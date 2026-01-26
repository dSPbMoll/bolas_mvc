package asteroid.dto;

import java.awt.*;

public class SpriteSheetDto {
    public final Image SPRITE_SHEET;
    public final int MAX_SPRITE_INDEX;
    public final Image[] SPRITES;

    public SpriteSheetDto(Image spriteSheet, int maxCurrentSpriteIndex, Image[] sprites) {
        this.SPRITE_SHEET = spriteSheet;
        this.MAX_SPRITE_INDEX = maxCurrentSpriteIndex;
        this.SPRITES = sprites;
    }

}
