package dev.ohate.survivalmultiplayer.util.arguments;

import dev.jorel.commandapi.arguments.*;
import dev.ohate.survivalmultiplayer.cache.UUIDCache;
import dev.ohate.survivalmultiplayer.module.playerdata.handler.PlayerDataHandler;
import dev.ohate.survivalmultiplayer.module.playerdata.model.PlayerData;
import dev.ohate.survivalmultiplayer.util.Message;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.concurrent.CompletableFuture;

public class ProfileArgument extends CustomArgument<CompletableFuture<PlayerData>, String> {

    public static Component ERROR = Message.error("A player with that name does not exist.");

    public ProfileArgument(String name) {
        super(new StringArgument(name), info -> {

            String playerName = info.input();
            Player player = Bukkit.getPlayer(playerName);

            if (player != null && player.isOnline()) {
                PlayerDataHandler handler = PlayerDataHandler.getInstance();

                return CompletableFuture.completedFuture(handler.getLocalPlayerData(player.getUniqueId()));
            }

            return UUIDCache.getUuid(name).thenApply(uuid -> {
                return uuid != null ? PlayerDataHandler.getInstance().retrieveFromMongo(uuid) : null;
            });
        });

        replaceSuggestions(ArgumentSuggestions.strings(info -> Bukkit.getOnlinePlayers()
                .stream()
                .map(Player::getName)
                .toArray(String[]::new)));
    }

}
