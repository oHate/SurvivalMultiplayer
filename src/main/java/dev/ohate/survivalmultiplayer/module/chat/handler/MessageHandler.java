package dev.ohate.survivalmultiplayer.module.chat.handler;

import dev.ohate.survivalmultiplayer.framework.Handler;
import dev.ohate.survivalmultiplayer.framework.Module;
import dev.ohate.survivalmultiplayer.util.Message;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MessageHandler extends Handler {

    @Getter
    private static MessageHandler instance;

    private final Map<UUID, UUID> lastMessage = new HashMap<>();

    public MessageHandler() {
        instance = this;
    }

    public void sendMessage(UUID sender, UUID receiver, String message) {
        Player senderPlayer = Bukkit.getPlayer(sender);
        Player receiverPlayer = Bukkit.getPlayer(receiver);

        if (senderPlayer == null || receiverPlayer == null) {
            return;
        }

        sendMessage(senderPlayer, receiverPlayer, message);
    }

    public void sendMessage(Player sender, Player receiver, String message) {
        UUID senderId = sender.getUniqueId();
        UUID receiverId = receiver.getUniqueId();

        lastMessage.put(senderId, receiverId);
        lastMessage.put(receiverId, senderId);

        sender.sendMessage(Component.textOfChildren(
                Component.text("(To ", NamedTextColor.GRAY),
                receiver.displayName(),
                Component.text(") " + message, NamedTextColor.GRAY)
        ));

        receiver.sendMessage(Component.textOfChildren(
                Component.text("(From ", NamedTextColor.GRAY),
                sender.displayName(),
                Component.text(") " + message, NamedTextColor.GRAY)
        ));
    }

    public boolean hasLastMessaged(Player player) {
        return lastMessage.containsKey(player.getUniqueId());
    }

    public Player getLastMessaged(Player player) {
        return hasLastMessaged(player) ? Bukkit.getPlayer(lastMessage.get(player.getUniqueId())) : null;
    }

    public void removeLastMessaged(Player player) {
        lastMessage.remove(player.getUniqueId());
    }

    @Override
    public Module getModule() {
        return null;
    }

}
