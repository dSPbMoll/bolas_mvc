package communications;

import java.io.IOException;
import java.net.Socket;
import java.util.Map;
import static java.lang.Thread.sleep;

public class ClientConnector implements Runnable {
    private final CommsController commsController;

    public ClientConnector(CommsController commsController) {
        this.commsController = commsController;
    }

    @Override
    public void run() {
        while (true) {

            for (Map.Entry<String, Channel> entry : commsController.getChannelsDown().entrySet()) {
                Channel channel = null;
                try {
                    channel = entry.getValue();
                    Socket socket = new Socket(channel.getClientAddress(), channel.getPort());
                    commsController.reviveChannelSocket(channel.getPlayer(), socket);
                } catch(IOException e) {
                    System.err.println("Failed to connect to " + channel.getPlayer());
                }
            }

            try {
                sleep(50);
            } catch(Exception ignored) {}

        }
    }


}
