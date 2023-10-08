package dev.ohate.survivalmultiplayer.util.arguments;

import dev.jorel.commandapi.arguments.*;
import dev.ohate.survivalmultiplayer.util.Message;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlayerArgument extends CustomArgument<Player, String> {

    public PlayerArgument(String name) {
        super(new StringArgument(name), info -> {
            Player player = Bukkit.getPlayer(info.input());

            if (player == null) {
                throw CustomArgument.CustomArgumentException.fromAdventureComponent(
                        Message.error("That player is not online.")
                );
            }

            return player;
        });

        replaceSuggestions(ArgumentSuggestions.strings(info -> Bukkit.getOnlinePlayers()
                .stream()
                .map(Player::getName)
                .toArray(String[]::new)));
    }

}
