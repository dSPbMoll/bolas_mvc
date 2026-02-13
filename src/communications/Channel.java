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
        this.healthChecker.startThread();
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
        this.lastSeen = System.currentTimeMillis();
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
            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            in  = new ObjectInputStream(socket.getInputStream());
        } catch (Exception e) {
            System.out.println("Error while preparing streams in Channel: " + e);
        }
    }

    @Override
    public void run() {
        while (running) {
            try {
                if (in != null && out != null) processClient();
            } catch (Exception e) {
                System.out.println("Error while processing client in channel (" + player + "): " + e);
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
        if (out == null) return;
        synchronized (out) {
            try {
                if (out != null) {
                    out.writeObject(new Frame(CommType.HEALTH, null));
                    out.flush();
                }
            } catch (IOException e) {
                killSocket();
            }
        }
    }

    public void killSocket() {
        try {
            if (socket != null) socket.close();
        } catch (IOException e) {
            System.out.println("Error closing the connection, forcing close");
        }

        System.out.println("Client (" + clientAddress + " : " + port + ") connection closed\n");
        this.socket = null;
        this.in = null;
        this.out = null;
        commsController.registerChannelKilled(this.player);
        this.stopThread();
        healthChecker.stopThread();
    }

    public void awakeSocket(Socket socket) {
        this.socket = socket;
        prepareStreams();
        this.startThread();
        healthChecker.startThread();
        System.out.println("Channel prepared with player: " + this.player);
    }

    public void sendFrame(Frame frame) {
        synchronized (out) {
            try {
                out.writeObject(frame);
                out.flush();
            } catch(IOException e) {
                System.err.println("Couldn't send frame: " + e);
            }
        }
    }
}
