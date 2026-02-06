package communications;

import communications.dto.CommType;
import communications.dto.Frame;

import java.io.*;
import java.net.*;

public class Channel implements Runnable {
    private final CommsController commsController;
    private Socket socket;
    private final String clientAddress;
    private final int port;
    private final String player;
    private Thread thread;
    private boolean running;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private final HealthChecker healthChecker;
    private long lastSeen;

    public Channel(CommsController commsController, String clientAddress, int port, String player) {
        this.commsController = commsController;
        this.clientAddress = clientAddress;
        this.port = port;
        this.player = player;
        this.lastSeen = System.currentTimeMillis();
        this.healthChecker = new HealthChecker(this);
    }

    public String getPlayer() {
        return this.player;
    }

    public int getPort() {
        return this.port;
    }

    public String getClientAddress() {
        return this.clientAddress;
    }

    public long getLastSeen() {
        return lastSeen;
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

    public void prepareStreams() {
        try {
            // Get IO streams from the socket
            in  = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
        } catch (Exception e) {
            System.out.println("Error in Channel's run(): " + e);
            System.out.println("Connection stablished with " + clientAddress + " (" + player + ")");
        }
    }

    @Override
    public void run() {
        while (running) {
            try {
                processClient();
            }
            catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    private void processClient() {
        try {
            Frame frame = (Frame) in.readObject();
            lastSeen = System.currentTimeMillis();

            if (frame != null) {
                if (frame.header != CommType.HEALTH) {
                    processEnteringFrame(frame);
                }
            }
        } catch(IOException e) {
            running = false;
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void processEnteringFrame(Frame frame) {
        if (frame.header == CommType.HEALTH) {
            this.lastSeen = System.currentTimeMillis();
        } else {
            commsController.sendEnteringFrameToApp(frame);
        }
    }

    // =============================== HEATH CHECKING ===============================

    public void sendHealthPing() {
        try {
            out.writeObject(new Frame(CommType.HEALTH, null));
            out.flush();
        } catch (IOException e) {
            killSocket();
        }
    }

    public void killSocket() {
        try {
            socket.close();
        } catch (IOException e) {
            System.out.println("Error closing the connection, forcing close");
        }

        System.out.println("Client (" + clientAddress + ") connection closed\n");
        this.socket = null;
        this.in = null;
        this.out = null;
        commsController.registerChannelKilled(this.player);
    }

    public void awakeSocket(Socket socket) {
        this.socket = socket;
        prepareStreams();
    }

    public void sendFrame(Frame frame) {
        try {
            out.writeObject(frame);
            out.flush();
        } catch(IOException e) {
            System.err.println("Couldn't send frame");
        }
    }
}
