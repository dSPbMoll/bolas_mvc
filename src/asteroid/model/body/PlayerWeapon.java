package asteroid.model.body;

import static java.lang.Thread.sleep;

public class PlayerWeapon implements Runnable {
    protected Thread thread;
    protected volatile boolean running;
    protected BodyPlayer player;
    protected int shootLatency;
    protected volatile boolean shooting;
    protected int bulletSpeed;

    public PlayerWeapon(BodyPlayer player) {
        this.player = player;
        this.shooting = false;
        this.shootLatency = 150;
        this.bulletSpeed = 15;
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
        while (running) {
            if (shooting) {
                try {
                    this.player.shot(this.bulletSpeed);

                    sleep(shootLatency);
                } catch (InterruptedException ignored) {}
            }
        }
    }

    // ==================================== GETTERS & SETTERS ====================================

    public void setShooting(boolean b) {
        this.shooting = b;

    }
}
