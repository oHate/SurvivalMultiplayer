package dev.ohate.survivalmultiplayer.framework;

import dev.ohate.survivalmultiplayer.SurvivalMultiplayer;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public abstract class Framework extends JavaPlugin {

    @Getter
    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

    private final List<Module> enabledModules = new ArrayList<>();

    public abstract List<Module> getModules();

    public void onEnable() {
        try {
            loadModules();
        } catch (Exception e) {
            e.printStackTrace();
            shutdown();
            return;
        }

        executor.scheduleAtFixedRate(this::saveModules, 3, 3, TimeUnit.MINUTES);
    }

    public void onDisable() {
        disableModules();
    }

    public void shutdown() {
        System.exit(1);
    }

    public void loadModules() {
        for (Module module : getModules()) {
            long start = System.currentTimeMillis();
            getLogger().info("Loading " + module.getName() + " module...");

            module.setLoaded(true);
            module.onEnable();

            for (Listener listener : module.getListeners()) {
                Bukkit.getPluginManager().registerEvents(listener, SurvivalMultiplayer.getInstance());
            }

            for (Class<?> command : module.getCommands()) {
                try {
                    command.getDeclaredConstructor().newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            enabledModules.add(module);
            getLogger().info("Enabled " + module.getName() + " Module! " + (System.currentTimeMillis() - start) + "ms to load.");
        }
    }

    public void disableModules() {
        for (Module module : enabledModules) {
            getLogger().info("Disabling " + module.getName() + " module...");

            try {
                module.onDisable();
                module.setLoaded(false);

                getLogger().info("Disabled " + module.getName() + " module!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void saveModules() {
        getModules().forEach(module -> {
            try {
                module.onAutoSave();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

}
