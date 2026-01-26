package asteroid;

import asteroid.controller.Controller;
import config.network.ServerConfig;
import config.player.ControlConfig;
import config.simulation.AsteroidConfig;
import config.simulation.WorldConfig;
import helpers.ConfigProcessor;

import java.util.HashMap;

public class AppLauncher {
    public static void main(String[] args) {
        ConfigProcessor configProcessor = new ConfigProcessor();

        // Defining strings
        String configFolderPath = "src/config/";

        String networkFolder = "network/";
        String playerFolder = "player/";
        String simulationFolder = "simulation/";

        String networkConfigFileName = "network_config.ini";
        String serverSectionName = "SERVER";

        String playerConfigFileName = "player_config.ini";
        String controlsSectionName = "CONTROLS";

        String simulationConfigFileName = "simulation_config.ini";
        String worldSectionName = "WORLD";
        String asteroidSectionName = "ASTEROID";

        // Getting configurations
        configProcessor.setWorkingFilePath(configFolderPath + playerFolder + playerConfigFileName);
        HashMap<ControlConfig, String> playerControlConfigs = configProcessor.processSection(
                controlsSectionName,
                ControlConfig.class,
                s -> s
        );

        configProcessor.setWorkingFilePath(configFolderPath + networkFolder + networkConfigFileName);
        HashMap<ServerConfig, String> serverConfigs = configProcessor.processSection(serverSectionName,
                ServerConfig.class,
                s -> s
        );

        configProcessor.setWorkingFilePath(configFolderPath + simulationFolder + simulationConfigFileName);
        HashMap<WorldConfig, Integer> worldConfigs = configProcessor.processSection(
                worldSectionName,
                WorldConfig.class,
                Integer::parseInt
        );
        HashMap<AsteroidConfig, Integer> asteroidConfigs = configProcessor.processSection(
                asteroidSectionName,
                AsteroidConfig.class,
                Integer::parseInt
        );

        Controller controller = new Controller(
                playerControlConfigs,
                worldConfigs,
                asteroidConfigs
        );
    }
}