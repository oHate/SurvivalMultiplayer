package dev.ohate.survivalmultiplayer.module.player.listener;

import dev.ohate.survivalmultiplayer.module.player.PlayerModule;
import dev.ohate.survivalmultiplayer.util.Colors;
import dev.ohate.survivalmultiplayer.util.Message;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.List;

public class PlayerListener implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getPlayer();
        Location loc = player.getLocation();

        HoverEvent<Component> hoverEvent = HoverEvent.showText(Component.textOfChildren(
                Component.text("You died at: ", NamedTextColor.GRAY),
                Component.join(Message.SPACE_SEPARATOR, List.of(
                        Component.text(loc.blockX(), Colors.CYAN),
                        Component.text(loc.blockY(), Colors.CYAN),
                        Component.text(loc.blockZ(), Colors.CYAN),
                        Component.text(loc.getWorld().getName(), Colors.CYAN)
                ))).applyFallbackStyle(Message.DEFAULT_STYLE));

        player.sendMessage(Component.space());
        player.sendMessage(Message.message(Component.text("You died! Hover to view your death location.", NamedTextColor.GRAY)).hoverEvent(hoverEvent));
        player.sendMessage(Component.space());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.joinMessage(Component.textOfChildren(
                Component.text("<", NamedTextColor.DARK_GRAY, TextDecoration.BOLD),
                Component.text("+", NamedTextColor.DARK_GREEN, TextDecoration.BOLD),
                Component.text("> ", NamedTextColor.DARK_GRAY, TextDecoration.BOLD),
                Component.text(event.getPlayer().getName() + " has joined the game.", NamedTextColor.GREEN)
        ));
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.quitMessage(Component.textOfChildren(
                Component.text("<", NamedTextColor.DARK_GRAY, TextDecoration.BOLD),
                Component.text("-", NamedTextColor.DARK_RED, TextDecoration.BOLD),
                Component.text("> ", NamedTextColor.DARK_GRAY, TextDecoration.BOLD),
                Component.text(event.getPlayer().getName() + " has left the game.", NamedTextColor.RED)
        ));
    }

}
