package dev.ohate.survivalmultiplayer.module.playerdata.handler;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import dev.ohate.survivalmultiplayer.SurvivalMultiplayer;
import dev.ohate.survivalmultiplayer.framework.Handler;
import dev.ohate.survivalmultiplayer.framework.Module;
import dev.ohate.survivalmultiplayer.module.playerdata.PlayerDataModule;
import dev.ohate.survivalmultiplayer.module.playerdata.model.PlayerData;
import dev.ohate.survivalmultiplayer.util.Scheduler;
import dev.ohate.survivalmultiplayer.util.json.Json;
import lombok.Getter;
import org.bson.Document;

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
        PlayerData.getCollection().replaceOne(
                Filters.eq(playerData.getId()),
                Json.writeToDocument(playerData),
                new ReplaceOptions().upsert(true)
        );
    }

    public PlayerData getLocalPlayerData(UUID uuid) {
        return playerData.get(uuid);
    }

    public PlayerData retrieveFromMongo(UUID uuid) {
        Document document = PlayerData.getCollection().find(Filters.eq("uuid", uuid.toString())).first();

        return document != null ? Json.readFromDocument(document, PlayerData.class) : null;
    }

    public PlayerData getOrCreatePlayerData(UUID uuid, String username) {
        PlayerData playerData = retrieveFromMongo(uuid);

        return playerData != null ? playerData : new PlayerData(uuid, username);
    }

}
