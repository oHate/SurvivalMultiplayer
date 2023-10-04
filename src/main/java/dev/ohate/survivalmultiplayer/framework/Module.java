package dev.ohate.survivalmultiplayer.framework;

import com.google.gson.JsonObject;
import dev.ohate.survivalmultiplayer.util.json.Json;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Listener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public abstract class Module {

    private final Logger logger = LoggerFactory.getLogger(getName());

    private JsonObject config = loadConfig();
    private boolean loaded = false;

    public List<Handler> cachedHandlers = null;

    public abstract Framework getFramework();

    public abstract String getName();

    public abstract String getConfigFileName();

    public boolean isEnabled() {
        return getFramework().getModules().contains(this);
    }

    public File getConfigFile() {
        return new File(getFramework().getDataFolder(), getConfigFileName() + ".json");
    }

    public JsonObject loadConfig() {
        File configFile = getConfigFile();

        try {
            return Json.readFromJson(Files.readString(configFile.toPath()), JsonObject.class);
        } catch (Exception ignored) {
            return new JsonObject();
        }
    }

    public void saveConfig() {
        try {
            Files.writeString(getConfigFile().toPath(), Json.writeToJson(config));
        } catch (IOException e) {
            getLogger().error("An error has occurred while trying to save the config:", e);
        }
    }

    public List<Handler> getCachedHandlers() {
        if (cachedHandlers == null) {
            cachedHandlers = getHandlers();
        }

        return cachedHandlers;
    }

    public void onEnable() {
        for (Handler handler : getCachedHandlers()) {
            try {
                handler.initialLoad();
                handler.setLoaded(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void onDisable() {
        for (Handler handler : getCachedHandlers()) {
            try {
                if (handler.isLoaded()) {
                    handler.saveData();
                }

                handler.setLoaded(false);
            } catch (Exception e) {
                logger.error("Failed to disable " + getName() + "handler:", e);
            }
        }
    }

    public void onReload() {
        config = loadConfig();
    }

    public void onAutoSave() {
        for (Handler handler : getCachedHandlers()) {
            try {
                if (handler.isLoaded()) {
                    handler.saveData();
                }
            } catch (Exception e) {
                logger.error("Failed to save " + getName() + " handler:", e);
            }
        }
    }

    public List<Handler> getHandlers() {
        return new ArrayList<>();
    }

    public List<Listener> getListeners() {
        return new ArrayList<>();
    }

    public List<Class<?>> getCommands() {
        return new ArrayList<>();
    }

}
