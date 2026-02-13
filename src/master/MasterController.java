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

import java.awt.geom.Point2D;
import java.util.HashMap;

public class MasterController {
    private GameController gameController;
    private CommsController commsController;
    private int playerNum;
    private int worldWidth;
    private int worldHeight;

    public MasterController() {
        ConfigsDto configs = extractConfigs();
        this.playerNum = configs.selfConfig.get(SelfConfig.PLAYER_NUM);

        this.gameController = new GameController(this,
                configs.playerControlConfigs,
                configs.worldConfigs,
                configs.asteroidConfigs);

        this.commsController = new CommsController(this, configs.serverConfigs, configs.selfConfig);

        this.worldWidth = configs.worldConfigs.get(WorldConfig.WIDTH);
        this.worldHeight = configs.worldConfigs.get(WorldConfig.HEIGHT);
    }

    public void setPlayerNum(int playerNum) {
        this.playerNum = playerNum;
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
        gameController.deleteEntity(movingBody.getType(), movingBody.getEntityId());
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

        System.out.println("Master controller sending frame");

        return new Frame(CommType.APP, payloadComms);
    }

    private Frame transformEnteringFramePosition(Frame frame) {
        Frame newFrame = null;
        PayloadComms newPayload = null;
        PayloadComms oldPayload = frame.payload;
        if (playerNum == 1) {
            newFrame = new Frame(
                    frame.header,
                    new PayloadComms(
                            oldPayload.type,
                            new Point2D.Double(worldWidth -1, oldPayload.position.y),
                            oldPayload.speed,
                            oldPayload.size,
                            oldPayload.rotationAngle,
                            oldPayload.assetId
                    )
            );

        } else if (playerNum == 2) {
            newFrame = new Frame(
                    frame.header,
                    new PayloadComms(
                            oldPayload.type,
                            new Point2D.Double(1, oldPayload.position.y),
                            oldPayload.speed,
                            oldPayload.size,
                            oldPayload.rotationAngle,
                            oldPayload.assetId
                    )
            );
        }
        return newFrame;
    }

    public void sendEnteringFrameToApp(Frame frame) {
        System.out.println("Master controller reciving frame from comms controller:");
        Frame frameForSend = transformEnteringFramePosition(frame);


        PayloadComms payload = frameForSend.payload;
        String message = String.format("""
                Header: %s
                Payload:
                    type: %s
                    position: (%f, %f)
                    speed: (%f, %f)
                    size: (%f, %f)
                    rotationAngle: %f
                    assetId: %d
                """,
                frameForSend.header,
                payload.type,
                payload.position.x, payload.position.y,
                payload.speed.x, payload.speed.y,
                payload.size.x, payload.size.y,
                payload.rotationAngle,
                payload.assetId);
        System.out.println(message);


        gameController.processEnteringBody(frameForSend.payload);
    }
}
