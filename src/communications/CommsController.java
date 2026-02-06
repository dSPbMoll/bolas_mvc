package communications;

import communications.dto.Frame;
import config.network.PlayerConfig;
import config.network.SelfConfig;
import master.MasterController;

import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CommsController {
    private final MasterController masterController;
    private final HashMap<String, Channel> channels;
    private ClientConnector clientConnector;
    private ServerConnector serverConnector;
    private final HashMap<Integer, HashMap<PlayerConfig, String>> playersConfigs;
    private final HashMap<SelfConfig, Integer> selfConfig;
    private final ConcurrentHashMap<String, Channel> channelsDown;
    private final String selfIP;
    private final int selfPort;

    public CommsController(
            MasterController masterController,
            HashMap<Integer, HashMap<PlayerConfig, String>> playersConfigs,
            HashMap<SelfConfig, Integer> selfConfig) {

        this.masterController = masterController;
        this.channels = new HashMap<>();
        this.playersConfigs = playersConfigs;
        this.selfConfig = selfConfig;
        this.channelsDown = new ConcurrentHashMap<>();

        String[] networkVals = getNetworkValsFromConfigs();
        this.selfIP = networkVals[0];
        this.selfPort = Integer.parseInt(networkVals[1]);

        initChannelsDown();

        this.clientConnector = new ClientConnector(this);
        this.serverConnector = new ServerConnector(this, selfPort);
    }

    public ConcurrentHashMap<String, Channel> getChannelsDown() {
        return this.channelsDown;
    }

    private void initChannelsDown() {
        for (Map.Entry<Integer, HashMap<PlayerConfig, String>> entry : playersConfigs.entrySet()) {
            if (entry.getValue().get(PlayerConfig.IP).equals(selfIP)) continue;

            String ip = entry.getValue().get(PlayerConfig.IP);
            int port = Integer.parseInt(entry.getValue().get(PlayerConfig.PORT));
            String player = "PLAYER" + entry.getKey();

            channelsDown.put(player, new Channel(this, ip, port, player));
        }
    }

    public void registerChannelKilled(String player) {
        Channel channel = channels.get(player);
        channelsDown.put(player, channel);
    }

    public void reviveChannelSocket(String player, Socket socket) {
        channelsDown.remove(player);
        Channel channel = channels.get(player);
        channel.awakeSocket(socket);
    }

    private String[] getNetworkValsFromConfigs() {
        int selfPlayerNum = selfConfig.get(SelfConfig.PLAYER_NUM);
        HashMap<PlayerConfig, String> selfPlayer = playersConfigs.get(selfPlayerNum);

        String selfIP = null;
        try {
            selfIP = InetAddress.getLocalHost().getHostAddress();
            System.out.println("Your IP is: " + selfIP);
        } catch(UnknownHostException e) {
            System.out.println("Failed getting selfIP in CommsController");
        }

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

    public void sendFrameToPlayerWithNum(int playerNum, Frame frame) {
        Channel sendingChannel = channels.get("PLAYER" + playerNum);
        if (!channelsDown.contains(sendingChannel)) sendingChannel.sendFrame(frame);
    }

    public void sendEnteringFrameToApp(Frame frame) {
        masterController.sendEnteringFrameToApp(frame);
    }
}
