package master;

import asteroid.controller.GameController;
import asteroid.model.EventType;
import asteroid.model.body.MovingBody;
import asteroid.model.physics.ScalarPhysicalVariable;
import communications.CommsController;
import communications.dto.CommType;
import communications.dto.Frame;
import communications.dto.PayloadComms;
import config.ConfigsDto;
import config.network.PlayerConfig;
import config.network.SelfConfig;
import config.player.ControlConfig;
import config.simulation.AsteroidConfig;
import config.simulation.WorldConfig;
import helpers.ConfigProcessor;

import java.util.HashMap;

public class MasterController {
    private GameController gameController;
    private CommsController commsController;
    private final int playerNum;

    public MasterController() {
        ConfigsDto configs = extractConfigs();
        this.playerNum = configs.selfConfig.get(SelfConfig.PLAYER_NUM);

        this.gameController = new GameController(this,
                configs.playerControlConfigs,
                configs.worldConfigs,
                configs.asteroidConfigs);

        this.commsController = new CommsController(this, configs.serverConfigs, configs.selfConfig);
    }

    private ConfigsDto extractConfigs() {
        ConfigProcessor configProcessor = new ConfigProcessor();

        // Defining strings
        String configFolderPath = "src/config/";

        String networkFolder = "network/";
        String playerFolder = "player/";
        String simulationFolder = "simulation/";

        String networkConfigFileName = "network_config.ini";
        String playerConfigFileName = "player_config.ini";
        String simulationConfigFileName = "simulation_config.ini";

        String controlsSectionName = "CONTROLS";
        String selfSectionName = "SELF";
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
        HashMap<Integer, HashMap<PlayerConfig, String>> playersConfigs = new HashMap<>();
        playersConfigs.put(
                1,
                configProcessor.processSection(
                        "PLAYER" + 1,
                        PlayerConfig.class,
                        s -> s)
        );
        playersConfigs.put(
                2,
                configProcessor.processSection(
                        "PLAYER" + 2,
                        PlayerConfig.class,
                        s -> s)
        );

        HashMap<SelfConfig, Integer> selfConfigs = configProcessor.processSection(
                selfSectionName,
                SelfConfig.class,
                Integer::parseInt
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

        return new ConfigsDto(playerControlConfigs, playersConfigs, selfConfigs, worldConfigs, asteroidConfigs);
    }

    public void movingBodyEventManager(EventType event, MovingBody movingBody) {
        switch (event) {
            case EAST_LIMIT_REACHED:
                if (playerNum == 1) {
                    Frame frame = transformMovingBodyIntroFrame(movingBody);
                    commsController.sendFrameToPlayerWithNum(2, frame);
                }
                break;

            case WEST_LIMIT_REACHED:
                if (playerNum == 2) {
                    Frame frame = transformMovingBodyIntroFrame(movingBody);
                    commsController.sendFrameToPlayerWithNum(1, frame);
                }
                break;
        }
        gameController.deleteEntity(movingBody.getType(), movingBody.getEntityId());
    }

    private Frame transformMovingBodyIntroFrame(MovingBody movingBody) {
        PayloadComms payloadComms = new PayloadComms(
                movingBody.getType(),
                movingBody.getPosition(),
                movingBody.getSpeed(),
                movingBody.getSize(),
                movingBody.getScalarPhysicalValue(ScalarPhysicalVariable.ROTATION_ANGLE),
                0
        );

        return new Frame(CommType.APP, payloadComms);
    }

    public void sendEnteringFrameToApp(Frame frame) {
        gameController.processEnteringBody(frame.payload);
    }
}
