package dev.ohate.survivalmultiplayer.module.itemfilter.listener;

import dev.ohate.survivalmultiplayer.module.playerdata.handler.PlayerDataHandler;
import dev.ohate.survivalmultiplayer.module.playerdata.model.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;

public class ItemFilterListener implements Listener {

    @EventHandler
    public void onEntityPickupItem(EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }

        PlayerDataHandler handler = PlayerDataHandler.getInstance();
        PlayerData date = handler.getPlayerData().get(player.getUniqueId());

        event.setCancelled(date.isItemFiltered(event.getItem().getItemStack().getType()));
    }

}
