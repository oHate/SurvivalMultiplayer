package dev.ohate.survivalmultiplayer.module.simple.listener;

import org.bukkit.Axis;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Orientable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.HashSet;
import java.util.Set;

public class CustomPortalListener implements Listener {

    private final Set<Material> VALID_PORTAL_MATERIALS = Set.of(
            Material.OBSIDIAN,
            Material.CRYING_OBSIDIAN
    );

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onNetherPortalLit(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Block clickedBlock = event.getClickedBlock();

        if (event.getAction() != Action.RIGHT_CLICK_BLOCK ||
                event.getHand() != EquipmentSlot.HAND ||
                event.getItem() == null ||
                clickedBlock == null ||
                event.getItem().getType() != Material.FLINT_AND_STEEL ||
                player.getWorld().getEnvironment() == Environment.THE_END ||
                !VALID_PORTAL_MATERIALS.contains(clickedBlock.getType()) ||
                clickedBlock.getRelative(event.getBlockFace()).getType() != Material.AIR) {
            return;
        }

        boolean facingEastWest = player.getFacing() == BlockFace.EAST || player.getFacing() == BlockFace.WEST;
        BlockFace[] faces = getRelativeBlockFaces(facingEastWest);

        Set<Block> checked = new HashSet<>();

        Block block = clickedBlock.getRelative(event.getBlockFace());
        PortalBounds bounds = new PortalBounds(block.getLocation(), facingEastWest);

        if (checkBlock(block, checked, faces, bounds)) {
            event.setCancelled(true);
            buildPortal(checked, facingEastWest);
            return;
        }

        facingEastWest = !facingEastWest;
        bounds = new PortalBounds(block.getLocation(), facingEastWest);
        faces = getRelativeBlockFaces(facingEastWest);

        checked.clear();

        if (checkBlock(block, checked, faces, bounds)) {
            event.setCancelled(true);
            buildPortal(checked, facingEastWest);
        }
    }

    private boolean checkBlock(Block block, Set<Block> checked, BlockFace[] faces, PortalBounds bounds) {
        if (VALID_PORTAL_MATERIALS.contains(block.getType()) || checked.contains(block)) {
            return true;
        }

        if (block.getType() != Material.AIR && block.getType() != Material.FIRE) {
            return false;
        }

        checked.add(block);

        if (bounds.updateBounds(block.getLocation()) && !bounds.withinBounds()) {
            return false;
        }

        for (BlockFace face : faces) {
            if (!checkBlock(block.getRelative(face), checked, faces, bounds)) {
                return false;
            }
        }

        return true;
    }

    private void buildPortal(Set<Block> portalBlocks, boolean facingEastWest) {
        for (Block block : portalBlocks) {
            block.setType(Material.NETHER_PORTAL);

            Orientable orientable = (Orientable) block.getBlockData();

            orientable.setAxis(facingEastWest ? Axis.Z : Axis.X);
            block.setBlockData(orientable);
        }
    }

    private BlockFace[] getRelativeBlockFaces(boolean facingEastWest) {
        return new BlockFace[]{
                BlockFace.UP,
                facingEastWest ? BlockFace.SOUTH : BlockFace.EAST,
                BlockFace.DOWN,
                facingEastWest ? BlockFace.NORTH : BlockFace.WEST
        };
    }

    private class PortalBounds {

        private static final int MAX_WIDTH = 20;
        private static final int MAX_HEIGHT = 40;

        int minX;
        int maxX;
        int minY;
        int maxY;

        boolean facingEastWest;

        public PortalBounds(Location loc, boolean facingEastWest) {
            this.minX = facingEastWest ? loc.getBlockZ() : loc.getBlockX();
            this.maxX = facingEastWest ? loc.getBlockZ() : loc.getBlockX();
            this.minY = loc.getBlockY();
            this.maxY = loc.getBlockY();

            this.facingEastWest = facingEastWest;
        }

        public boolean updateBounds(Location loc) {
            boolean valueChanged = false;
            int curX = facingEastWest ? loc.getBlockZ() : loc.getBlockX();
            int curY = loc.getBlockY();

            if (curX < minX) {
                minX = curX;
                valueChanged = true;
            }

            if (curX >= maxX) {
                maxX = curX;
                valueChanged = true;
            }

            if (curY < minY) {
                minY = curY;
                valueChanged = true;
            }

            if (curY >= maxY) {
                maxY = curY;
                valueChanged = true;
            }

            return valueChanged;
        }

        public boolean withinBounds() {
            if ((maxX - minX) > MAX_WIDTH) {
                return false;
            }

            return (maxY - minY) <= MAX_HEIGHT;
        }
    }

}