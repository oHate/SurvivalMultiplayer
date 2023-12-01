package dev.ohate.survivalmultiplayer.module.simple.listener;

import dev.ohate.commonlib.Colors;
import dev.ohate.survivalmultiplayer.SurvivalMultiplayer;
import dev.ohate.survivalmultiplayer.util.C;
import dev.ohate.survivalmultiplayer.util.ExperienceManager;
import dev.ohate.survivalmultiplayer.util.Message;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class BottledExpListener implements Listener {

    private static final NamespacedKey KEY = new NamespacedKey(SurvivalMultiplayer.getInstance(), "exp");
    private static final Component DISPLAY_NAME = Component.text("Bottled Exp", C.wrap(Colors.Flat.BELIZEHOLE))
            .applyFallbackStyle(Message.DEFAULT_STYLE);

    public BottledExpListener() {
        Bukkit.addRecipe(new ShapelessRecipe(KEY, createExpBottle(1))
                .addIngredient(Material.GLASS_BOTTLE));
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();

        if (!event.hasItem() || (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK)) {
            return;
        }

        ItemStack itemStack = event.getItem();

        if (!isBottledExperience(itemStack)) {
            return;
        }

        event.setCancelled(true);

        ItemMeta meta = itemStack.getItemMeta();

        ExperienceManager.changeExp(player, meta.getPersistentDataContainer().get(KEY, PersistentDataType.INTEGER));

        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);
        itemStack.setAmount(itemStack.getAmount() - 1);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onPrepareCraft(PrepareItemCraftEvent event) {
        CraftingInventory inventory = event.getInventory();

        if (!(event.getInventory().getHolder() instanceof Player player) ||
                !isBottledExperience(inventory.getResult())) {
            return;
        }

        int exp = ExperienceManager.getCurrentExp(player);

        inventory.setResult(exp > 0 ? createExpBottle(exp) : new ItemStack(Material.AIR, 1));
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onCraftItem(CraftItemEvent event) {
        if (!(event.getWhoClicked() instanceof Player player) ||
                event.getSlotType() != InventoryType.SlotType.RESULT ||
                !isBottledExperience(event.getCurrentItem())) {
            return;
        }

        player.setLevel(0);
        player.setExp(0);
    }

    private ItemStack createExpBottle(int experience) {
        ItemStack itemStack = new ItemStack(Material.EXPERIENCE_BOTTLE, 1);
        ItemMeta meta = itemStack.getItemMeta();

        meta.displayName(DISPLAY_NAME);
        meta.lore(List.of(Component.textOfChildren(
                Component.text(experience, NamedTextColor.WHITE),
                Component.text(" Experience", C.wrap(Colors.Flat.BELIZEHOLE))
        ).applyFallbackStyle(Message.DEFAULT_STYLE)));
        meta.getPersistentDataContainer().set(KEY, PersistentDataType.INTEGER, experience);

        itemStack.setItemMeta(meta);

        return itemStack;
    }

    private boolean isBottledExperience(ItemStack itemStack) {
        if (itemStack == null || !itemStack.hasItemMeta()) {
            return false;
        }

        ItemMeta meta = itemStack.getItemMeta();
        return meta.getPersistentDataContainer().has(KEY, PersistentDataType.INTEGER);
    }

}