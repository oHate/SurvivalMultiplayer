package dev.ohate.survivalmultiplayer.module.playerdata.listener;

import dev.ohate.survivalmultiplayer.module.playerdata.handler.PlayerDataHandler;
import dev.ohate.survivalmultiplayer.module.playerdata.model.PlayerData;
import dev.ohate.survivalmultiplayer.util.Colors;
import dev.ohate.survivalmultiplayer.util.Scheduler;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class PlayerDataListener implements Listener {

    @EventHandler
    public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        Player player = Bukkit.getPlayer(event.getUniqueId());

        if (player != null && player.isOnline()) {
            event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
            event.kickMessage(Component.text("You tried to login too quickly after disconnecting.", Colors.RED));
            Scheduler.runTask(() -> player.kick(Component.text("Duplicate login kick", Colors.RED)));
            return;
        }

        UUID playerUuid = event.getUniqueId();
        String username = event.getName();

        PlayerDataHandler handler = PlayerDataHandler.getInstance();
        PlayerData playerData = handler.getOrCreatePlayerData(playerUuid, username);

        boolean save = false;

        if (!playerData.getUsername().equalsIgnoreCase(username)) {
            playerData.setUsername(username);
            save = true;
        }

        if (save) {
            handler.savePlayerData(playerData);
        }

        handler.getPlayerData().put(playerData.getUuid(), playerData);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        PlayerDataHandler handler = PlayerDataHandler.getInstance();
        handler.getPlayerData().remove(event.getPlayer().getUniqueId());
    }

}
