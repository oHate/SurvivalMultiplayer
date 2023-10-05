package dev.ohate.survivalmultiplayer;

import com.google.gson.JsonSyntaxException;
import com.mongodb.client.MongoDatabase;
import dev.ohate.survivalmultiplayer.framework.Framework;
import dev.ohate.survivalmultiplayer.framework.Module;
import dev.ohate.survivalmultiplayer.module.itemfilter.ItemFilterModule;
import dev.ohate.survivalmultiplayer.mongo.Mongo;
import dev.ohate.survivalmultiplayer.util.SurvivalMultiplayerConfig;
import dev.ohate.survivalmultiplayer.util.json.Json;
import lombok.Data;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@Getter
public class SurvivalMultiplayer extends Framework {

    @Getter
    private static SurvivalMultiplayer instance;

    private SurvivalMultiplayerConfig config;
    private Mongo mongo;
    private MongoDatabase database;

    private final List<Module> enabledModules = new ArrayList<>();

    @Override
    public void onEnable() {
        instance = this;

        loadConfig(new File(getDataFolder(), "config.json"));

        mongo = new Mongo();
        mongo.connect(config.getMongoUri());
        database = mongo.getClient().getDatabase("survivalmultiplayer");

        enabledModules.addAll(List.of(
                new ItemFilterModule()
        ));

        super.onEnable();
    }

    private void loadConfig(File configFile) {
        if (!configFile.exists()) {
            config = new SurvivalMultiplayerConfig();

            try {
                Files.writeString(configFile.toPath(), Json.writeToPrettyJson(config));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                config = Json.readFromJson(new String(Files.readAllBytes(configFile.toPath())), SurvivalMultiplayerConfig.class);
            } catch (IOException | JsonSyntaxException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public List<Module> getModules() {
        return enabledModules;
    }

}