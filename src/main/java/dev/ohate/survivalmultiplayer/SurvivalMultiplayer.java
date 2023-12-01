package dev.ohate.survivalmultiplayer;

import com.google.gson.JsonSyntaxException;
import com.mongodb.client.MongoDatabase;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import dev.ohate.survivalmultiplayer.database.Redis;
import dev.ohate.survivalmultiplayer.framework.Framework;
import dev.ohate.survivalmultiplayer.framework.Module;
import dev.ohate.survivalmultiplayer.framework.command.FrameworkCommand;
import dev.ohate.survivalmultiplayer.module.chat.ChatModule;
import dev.ohate.survivalmultiplayer.module.itemfilter.ItemFilterModule;
import dev.ohate.survivalmultiplayer.module.simple.SimpleModule;
import dev.ohate.survivalmultiplayer.module.playerdata.PlayerDataModule;
import dev.ohate.survivalmultiplayer.database.Mongo;
import dev.ohate.survivalmultiplayer.util.SurvivalMultiplayerConfig;
import dev.ohate.survivalmultiplayer.util.json.Json;
import lombok.Getter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@Getter
public class SurvivalMultiplayer extends Framework {

    @Getter
    private static SurvivalMultiplayer instance;

    private SurvivalMultiplayerConfig conf;
    private Redis redis;
    private Mongo mongo;
    private MongoDatabase database;

    private final List<Module> enabledModules = new ArrayList<>();

    @Override
    public void onEnable() {
        instance = this;

        CommandAPI.onLoad(new CommandAPIBukkitConfig(this));

        loadConfig(new File(getDataFolder(), "config.json"));

        new FrameworkCommand();

        redis = new Redis();
        redis.connect(conf.getRedisUri());

        mongo = new Mongo();
        mongo.connect(conf.getMongoUri());
        database = mongo.getClient().getDatabase("survivalmultiplayer");

        enabledModules.addAll(List.of(
                new PlayerDataModule(),
                new ItemFilterModule(),
                new SimpleModule(),
                new ChatModule()
        ));

        super.onEnable();
    }

    private void loadConfig(File configFile) {
        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();

            conf = new SurvivalMultiplayerConfig();

            try {
                Files.writeString(configFile.toPath(), Json.writeToPrettyJson(conf));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                conf = Json.readFromJson(new String(Files.readAllBytes(configFile.toPath())), SurvivalMultiplayerConfig.class);
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