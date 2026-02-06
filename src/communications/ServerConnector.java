package communications;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServerConnector implements Runnable {
    private final CommsController commsController;
    private final Thread thread;
    private ServerSocket serverSocket;

    public ServerConnector(CommsController commsController, int port) {
        this.commsController = commsController;
        this.thread = new Thread(this);

        try {
            this.serverSocket = new ServerSocket(port);
        } catch(IOException e) {
            System.err.println("CRITICAL ERROR: Could not initialize serverSocket in ServerConnector constructor.");
        }
        thread.start();
    }

    @Override
    public void run() {
        while (true) {

            try {
                Socket clientSock = serverSocket.accept();

                ConcurrentHashMap<String, Channel> channelsDown = commsController.getChannelsDown();

                for (Map.Entry<String, Channel> entry : channelsDown.entrySet()) {
                    Channel channel = entry.getValue();
                    if (clientSock.getInetAddress().getHostAddress().equals(channel.getClientAddress())) {
                        channel.awakeSocket(clientSock);
                    }
                }

            } catch(IOException e) {
                System.err.println("Couldn't accept client in ServerConnector");
            }

        }
    }
}
