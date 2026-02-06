package communications;

import static java.lang.Thread.sleep;

public class HealthChecker implements Runnable {
    private Channel channel;
    private Thread thread;
    private boolean running;

    public HealthChecker(Channel channel) {
        this.channel = channel;
        this.thread = new Thread();
        thread.start();
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

    public void run() {
        long timeout = 2000;
        while (running) {
            channel.sendHealthPing();

            long silentTime = System.currentTimeMillis() - channel.getLastSeen();

            if (silentTime > timeout) {
                System.out.println("Inactive channel for " + silentTime + "ms. Killing...");
                channel.killSocket();
                break;
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException ignored) {}
        }
    }
}
