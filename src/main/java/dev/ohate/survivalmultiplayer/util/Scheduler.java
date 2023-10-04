package dev.ohate.survivalmultiplayer.util;

import dev.ohate.survivalmultiplayer.SurvivalMultiplayer;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

public class Scheduler {

    public static BukkitTask runTask(Runnable runnable) {
        return Bukkit.getScheduler().runTask(SurvivalMultiplayer.getInstance(), runnable);
    }

    public static BukkitTask runTaskAsync(Runnable runnable) {
        return Bukkit.getScheduler().runTaskAsynchronously(SurvivalMultiplayer.getInstance(), runnable);
    }

}
