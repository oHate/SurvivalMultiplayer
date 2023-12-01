package dev.ohate.survivalmultiplayer.module.simple.listener;

import dev.ohate.survivalmultiplayer.SurvivalMultiplayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Leaves;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class FastLeafDecayListener implements Listener {

    private final Set<LeavesDecayEvent> fastDecayEvents = new HashSet<>();
    private final Random RANDOM = new Random();

    @EventHandler
    public void onLeafDecay(LeavesDecayEvent event) {
        if (fastDecayEvents.remove(event)) return;

        event.setCancelled(true);

        var block = event.getBlock();

        new BukkitRunnable() {
            @Override
            public void run() {
                List<Location> locations = getNearbyBlocks(block.getLocation(), 10);
                removeLeaves(locations);
            }
        }.runTaskAsynchronously(SurvivalMultiplayer.getInstance());
    }

    private void removeLeaves(List<Location> locations) {
        int duration = 10 * 20;

        for (Location location : locations) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(SurvivalMultiplayer.getInstance(), () -> {
                Block block = location.getBlock();

                if (!isLeaf(block)) {
                    return;
                }

                LeavesDecayEvent event = new LeavesDecayEvent(block);
                fastDecayEvents.add(event);

                Bukkit.getPluginManager().callEvent(event);

                if (event.isCancelled()) {
                    return;
                }

                block.breakNaturally();
                locations.remove(location);
            }, RANDOM.nextInt(duration));
        }
    }

    public List<Location> getNearbyBlocks(Location loc, int radius) {
        List<Location> locations = new ArrayList<>();

        for (int x = loc.getBlockX() - radius; x <= loc.getBlockX() + radius; x++) {
            for (int y = loc.getBlockY() - radius; y <= loc.getBlockY() + radius; y++) {
                for (int z = loc.getBlockZ() - radius; z <= loc.getBlockZ() + radius; z++) {
                    Block block = loc.getWorld().getBlockAt(x, y, z);

                    if (block.getType().isAir()) {
                        continue;
                    }

                    Bukkit.getScheduler().scheduleSyncDelayedTask(SurvivalMultiplayer.getInstance(), () -> {
                        if (!isLeaf(block)) {
                            return;
                        }

                        Leaves leaves = (Leaves) block.getBlockData();

                        if (leaves.isPersistent() ||
                                leaves.getDistance() <= 6 ||
                                locations.contains(block.getLocation())) {
                            return;
                        }

                        locations.add(block.getLocation());
                    });
                }
            }
        }

        return locations;
    }

    private boolean isLeaf(Block block) {
        return Tag.LEAVES.isTagged(block.getType());
    }

}
