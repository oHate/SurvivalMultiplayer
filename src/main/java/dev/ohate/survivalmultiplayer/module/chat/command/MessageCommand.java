package dev.ohate.survivalmultiplayer.module.chat.command;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import dev.ohate.survivalmultiplayer.module.chat.handler.MessageHandler;
import dev.ohate.survivalmultiplayer.util.Message;
import dev.ohate.survivalmultiplayer.util.arguments.PlayerArgument;
import org.bukkit.entity.Player;

public class MessageCommand {

    public MessageCommand() {
        new CommandAPICommand("message")
                .withAliases("msg", "tell", "w", "whisper")
                .withArguments(
                        new PlayerArgument("player"),
                        new GreedyStringArgument("message")
                ).executesPlayer((sender, args) -> {
                    Player receiver = (Player) args.get(0);
                    String message = (String) args.get(1);

                    if (sender == receiver) {
                        sender.sendMessage(Message.error("You cannot message yourself."));
                        return;
                    }

                    MessageHandler.getInstance().sendMessage(sender, receiver, message);
                }).register();
    }

}
