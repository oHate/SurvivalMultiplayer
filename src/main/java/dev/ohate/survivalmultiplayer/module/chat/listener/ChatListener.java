package dev.ohate.survivalmultiplayer.module.chat.listener;

import dev.ohate.commonlib.UnicodeUtil;
import dev.ohate.survivalmultiplayer.module.chat.handler.MessageHandler;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class ChatListener implements Listener {

    @EventHandler
    public void onAsyncChat(AsyncChatEvent event) {
        event.renderer((source, sourceDisplayName, message, viewer) -> Component.textOfChildren(
                Component.text("<", NamedTextColor.DARK_GRAY),
                sourceDisplayName.applyFallbackStyle(Style.style(NamedTextColor.GRAY)),
                Component.text(">", NamedTextColor.DARK_GRAY),
                Component.text(" " + UnicodeUtil.DASHED_RIGHT_ARROW + " ", NamedTextColor.DARK_GRAY),
                message
        ));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        MessageHandler.getInstance().removeLastMessaged(event.getPlayer());
    }

}
