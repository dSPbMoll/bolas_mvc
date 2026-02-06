package config;

import config.network.PlayerConfig;
import config.network.SelfConfig;
import config.player.ControlConfig;
import config.simulation.AsteroidConfig;
import config.simulation.WorldConfig;

import java.util.HashMap;

public class ConfigsDto {
    public final HashMap<ControlConfig, String> playerControlConfigs;
    public final HashMap<Integer, HashMap<PlayerConfig, String>> serverConfigs;
    public final HashMap<SelfConfig, Integer> selfConfig;
    public final HashMap<WorldConfig, Integer> worldConfigs;
    public final HashMap<AsteroidConfig, Integer> asteroidConfigs;

    public ConfigsDto(
            HashMap<ControlConfig, String> playerControlConfigs,
            HashMap<Integer, HashMap<PlayerConfig, String>> serverConfigs,
            HashMap<SelfConfig, Integer> selfConfig,
            HashMap<WorldConfig, Integer> worldConfigs,
            HashMap<AsteroidConfig, Integer> asteroidConfigs) {

        this.playerControlConfigs = playerControlConfigs;
        this.serverConfigs = serverConfigs;
        this.selfConfig = selfConfig;
        this.worldConfigs = worldConfigs;
        this.asteroidConfigs = asteroidConfigs;
    }
}
