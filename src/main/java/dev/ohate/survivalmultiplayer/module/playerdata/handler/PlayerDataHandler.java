package dev.ohate.survivalmultiplayer.module.playerdata.handler;

import dev.ohate.survivalmultiplayer.SurvivalMultiplayer;
import dev.ohate.survivalmultiplayer.framework.Handler;
import dev.ohate.survivalmultiplayer.framework.Module;
import dev.ohate.survivalmultiplayer.module.playerdata.PlayerDataModule;
import dev.ohate.survivalmultiplayer.module.playerdata.model.PlayerData;
import dev.ohate.survivalmultiplayer.util.Scheduler;
import dev.ohate.survivalmultiplayer.util.json.Json;
import lombok.Getter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public class PlayerDataHandler extends Handler {

    @Getter
    private static PlayerDataHandler instance;

    private final Map<UUID, PlayerData> playerData = new HashMap<>();

    public PlayerDataHandler() {
        instance = this;
    }

    @Override
    public Module getModule() {
        return PlayerDataModule.getInstance();
    }

    public void savePlayerDataAsync(PlayerData playerData) {
        Scheduler.runTaskAsync(() -> savePlayerData(playerData));
    }

    public void savePlayerData(PlayerData playerData) {
        File playerFile = new File(
                SurvivalMultiplayer.getInstance().getDataFolder(),
                "PlayerData/" + playerData.getUuid().toString() + ".json"
        );

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(playerFile))) {
            writer.write(Json.writeToJson(playerData));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public PlayerData getOrCreatePlayerData(UUID uuid, String username) {
        File playerFile = new File(
                SurvivalMultiplayer.getInstance().getDataFolder(),
                "PlayerData/" + uuid.toString() + ".json"
        );

        if (!playerFile.exists()) {
            return new PlayerData(uuid, username);
        } else {
            try {
                return Json.readFromJson(new String(Files.readAllBytes(playerFile.toPath())), PlayerData.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
