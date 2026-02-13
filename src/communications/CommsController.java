package communications;

import communications.dto.Frame;
import config.network.PlayerConfig;
import config.network.SelfConfig;
import master.MasterController;

import java.net.*;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class CommsController {
    private final MasterController masterController;
    private final ConcurrentHashMap<String, Channel> channelsUp;
    private final ConcurrentHashMap<String, Channel> channelsDown;
    private final ClientConnector clientConnector;
    private final ServerConnector serverConnector;
    private final HashMap<Integer, HashMap<PlayerConfig, String>> playersConfigs;
    private final HashMap<SelfConfig, Integer> selfConfig;
    private final String selfIP;
    private int selfPort;
    private int playerNum;

    public CommsController(
            MasterController masterController,
            HashMap<Integer, HashMap<PlayerConfig, String>> playersConfigs,
            HashMap<SelfConfig, Integer> selfConfig) {

        this.masterController = masterController;
        this.channelsUp = new ConcurrentHashMap<>();
        this.playersConfigs = playersConfigs;
        this.selfConfig = selfConfig;
        this.channelsDown = new ConcurrentHashMap<>();

        String[] networkVals = getNetworkValsFromConfigs();
        this.selfIP = networkVals[0];
        this.selfPort = Integer.parseInt(networkVals[1]);
        this.playerNum = selfConfig.get(SelfConfig.PLAYER_NUM);

        initChannelsDown();

        this.serverConnector = new ServerConnector(this, selfPort);
        this.clientConnector = new ClientConnector(this);
        serverConnector.startThread();
        clientConnector.startThread();
    }

    public ConcurrentHashMap<String, Channel> getChannelsDown() {
        return this.channelsDown;
    }

    private void initChannelsDown() {
        System.out.println("SelfPlayer: " + playerNum);
        for (Map.Entry<Integer, HashMap<PlayerConfig, String>> entry : playersConfigs.entrySet()) {
            if (Objects.equals(entry.getKey(), playerNum)) continue;

            String entryIp = entry.getValue().get(PlayerConfig.IP);
            int entryPort = Integer.parseInt(entry.getValue().get(PlayerConfig.PORT));
            if (entryIp.equals(selfIP) && entryPort == selfPort) continue;

            String player = "PLAYER" + entry.getKey();
            System.out.println("Adding to ChannelsDown: " + player);
            channelsDown.put(player, new Channel(this, entryIp, entryPort, player));
        }
    }

    public void registerChannelKilled(String player) {
        Channel channel = channelsUp.get(player);
        if (channel == null) return;
        channelsUp.remove(player);
        channelsDown.put(player, channel);
    }

    synchronized public void reviveChannelSocket(String player, Socket socket) {
        Channel channel = channelsDown.get(player);
        if (channel == null) {
            System.out.println("(CommsController) Channel is null. Returning");
            return;
        }

        channelsDown.remove(player);
        channelsUp.put(player, channel);
        channel.awakeSocket(socket);
    }

    private String[] getNetworkValsFromConfigs() {
        int selfPlayerNum = selfConfig.get(SelfConfig.PLAYER_NUM);
        HashMap<PlayerConfig, String> selfPlayer = playersConfigs.get(selfPlayerNum);

        String selfIP = getRealSelfIp();
        System.out.println("Your IP is: " + selfIP);


        String entryIP = selfPlayer.get(PlayerConfig.IP);
        String entryPort = selfPlayer.get(PlayerConfig.PORT);

        if (entryIP.equals(selfIP)) {
            // If our adapter IP actually matches the configurations of the player with our player num
            String[] networkVals = new String[2];
            networkVals[0] = selfIP;
            networkVals[1] = entryPort;

            return networkVals;
        } else {
            System.err.println("Couldn't initialize network values. Revise your network_config.ini file!");
        }

        return null;
    }

    public void sendFrameToPlayerWithNum(int destinyPlayerNum, Frame frame) {
        Channel sendingChannel = channelsUp.get("PLAYER" + destinyPlayerNum);

        //System.out.println("PLAYER NUM OF CONFIGS" + playerNum);
        //System.out.println("DESTINY PLAYER NUM: " + destinyPlayerNum);
        //printChannelEntries();


        if (sendingChannel != null) {
            sendingChannel.sendFrame(frame);
            //System.out.println("FRAME RECEIVED, IS NOT NULL!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        } else {
            //System.out.println("SENDING CHANNEL ES NULL ME CAGO EN DIOS");
        }
    }

    public void sendEnteringFrameToApp(Frame frame) {
        masterController.sendEnteringFrameToApp(frame);
    }

    private String getRealSelfIp(){
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();

            while (interfaces.hasMoreElements()) {
                NetworkInterface iface = interfaces.nextElement();

                // Saltar interfaces inactivas, loopback y virtuales
                if (!iface.isUp() || iface.isLoopback()) {
                    continue;
                }

                Enumeration<InetAddress> addresses = iface.getInetAddresses();

                while (addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();

                    // Only IPv4
                    if (addr instanceof Inet4Address) {
                        String ip = addr.getHostAddress();

                        // Buscar específicamente IPs de la red WifiWire (172.16.8.x)
                        if (ip.startsWith("172.25.26.")) {
                            return ip;
                        }
                    }
                }
            }

            System.out.println("No se encontró IP 172.16.8.x, usando getLocalHost()");
            return InetAddress.getLocalHost().getHostAddress();

        } catch (SocketException e) {
            System.err.println("Error obteniendo interfaces de red: " + e.getMessage());
            try {
                return InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException ex) {
                return "localhost";
            }
        } catch (UnknownHostException e) {
            System.err.println("Error obteniendo host local: " + e.getMessage());
            return "localhost";
        }
    }



    // =========================== TESTING IN SAME MACHINE ===================================

    public void enableTestingInSameMachine(int newPort) {
        this.selfPort = newPort;


        if (playerNum == 1) {
            playerNum = 2;
        } else if (playerNum == 2) {
            playerNum = 1;
        }

        masterController.setPlayerNum(playerNum);

        channelsDown.clear();
        initChannelsDown();
    }

    // ============================ DEBUG ================================
    public void printChannelEntries() {
        System.out.println("ChannelsUp: ");
        for (Map.Entry<String, Channel> entry : this.channelsUp.entrySet()) {
            System.out.println("Player: " + entry.getKey());
        }
        System.out.println();
        System.out.println("ChannelsDown: ");
        for (Map.Entry<String, Channel> entry : this.channelsDown.entrySet()) {
            System.out.println("Player: " + entry.getKey());
        }
    }
}
