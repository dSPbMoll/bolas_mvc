package communications;

import communications.dto.Frame;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServerConnector implements Runnable {
    private final CommsController commsController;
    private Thread thread;
    private volatile boolean running;
    private int port;
    private ServerSocket serverSocket;

    public ServerConnector(CommsController commsController, int port) {
        this.commsController = commsController;
        this.port = port;
        //System.out.println("ServerConnector CONSTRUCTOR_PORT: " + port);
        this.thread = new Thread(this);

        try {
            this.serverSocket = new ServerSocket(port);

        } catch(BindException e) {
            System.err.println("Failed opening serverSocket at PORT " + this.port + ", proceeding to open at PORT " + (this.port +1));
            try {
                this.port++;
                this.serverSocket = new ServerSocket(this.port);
                this.commsController.enableTestingInSameMachine(this.port);
            } catch(IOException e2) {
                System.err.println("Failed opening serverSocket at PORT " + (this.port) + ", connections are disabled");
            }

        } catch(IOException e) {
            System.err.println("CRITICAL ERROR: Could not initialize serverSocket in ServerConnector constructor due to an unexpected error");
        }

        thread.start();
    }

    public int getPort() {
        return this.port;
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
            System.out.println("SERVER_PORT at start of while(): " + port);
            try {
                Socket clientSock = serverSocket.accept();
                System.out.println("Finished waiting for accept in serverConnector");

                ConcurrentHashMap<String, Channel> channelsDown = commsController.getChannelsDown();

                for (Map.Entry<String, Channel> entry : channelsDown.entrySet()) {
                    Channel channel = entry.getValue();

                    if (clientSock.getInetAddress().getHostAddress().equals(channel.getClientAddress())) {
                        System.out.println("Awaking socket in serverConnector");
                        commsController.reviveChannelSocket(entry.getKey(), clientSock);
                    } else {
                        clientSock.close();
                    }
                }

            } catch(IOException e) {
                System.err.println("Couldn't accept client in ServerConnector");
            }
        }
        //System.err.println("CRITICAL ERROR: Server connector bucle ended");
    }
}
