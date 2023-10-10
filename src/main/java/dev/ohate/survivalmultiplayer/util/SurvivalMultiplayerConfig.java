package dev.ohate.survivalmultiplayer.util;

import lombok.Data;

@Data
public class SurvivalMultiplayerConfig {

    private String redisUri = "redis://127.0.0.1:6379/0";
    private String mongoUri = "mongodb://127.0.0.1:27017/";

}
