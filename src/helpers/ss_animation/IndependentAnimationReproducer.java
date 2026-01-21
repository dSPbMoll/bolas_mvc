package helpers.ss_animation;

import static java.lang.Thread.sleep;

public class IndependentAnimationReproducer implements Runnable {
    private Thread thread;
    private SpriteLoader manager;
    private String animName;
    private int delay;
    private int frameAmmount;
    private int x;
    private int y;
    private int width;
    private int height;

    public IndependentAnimationReproducer(SpriteLoader manager, String animName, int delay, int frameAmmmount, int x, int y, int width, int height) {
        this.thread = new Thread();

        this.manager = manager;
        this.animName = animName;
        this.delay = delay;
        this.frameAmmount = frameAmmmount;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void init() {
        this.thread.start();
    }

    @Override
    public void run() {
        for (int i=0; i < frameAmmount; i++) {

            try {
                sleep(delay);
            } catch(InterruptedException ignored) {}
        }
    }

}