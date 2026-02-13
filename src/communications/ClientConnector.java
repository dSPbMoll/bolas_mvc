package communications;

import java.io.IOException;
import java.net.BindException;
import java.net.Socket;
import java.util.Map;
import static java.lang.Thread.sleep;

public class ClientConnector implements Runnable {
    private final CommsController commsController;
    private Thread thread;
    private volatile boolean running;

    public ClientConnector(CommsController commsController) {
        this.commsController = commsController;
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
        while (true) {
            //System.out.println("CHANNELS_DOWN size at start of while() in clientConn: " + commsController.getChannelsDown().size());
            for (Map.Entry<String, Channel> entry : commsController.getChannelsDown().entrySet()) {
                Channel channel = null;
                try {
                    channel = entry.getValue();
                    System.out.println("Target port at clientConnector: " + channel.getPort());
                    Socket socket = new Socket(channel.getClientAddress(), channel.getPort());
                    System.out.println("Awaking socket in clientConnector");
                    commsController.reviveChannelSocket(channel.getPlayer(), socket);

                } catch(BindException e) {
                    //Testing connect into the same machine
                    try {
                        channel = entry.getValue();
                        Socket socket = new Socket(channel.getClientAddress(), channel.getPort());
                        commsController.reviveChannelSocket(channel.getPlayer(), socket);
                    }  catch(IOException e2) {
                        System.err.println("Failed to connect to " + channel.getPlayer() + " (LOCALHOST)");
                    }

                } catch(IOException e) {
                    System.err.println("Failed to connect to " + channel.getPlayer());
                }
                //System.out.println("CHANNELS_DOWN size at end of for(): " + commsController.getChannelsDown().size());
            }

            try {
                sleep(50);
            } catch(Exception ignored) {}

        }
        //System.err.println("CRITICAL ERROR: Client connector bucle ended");
    }


}
