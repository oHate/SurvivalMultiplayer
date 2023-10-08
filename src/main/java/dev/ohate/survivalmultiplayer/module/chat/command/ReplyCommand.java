package dev.ohate.survivalmultiplayer.module.chat.command;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import dev.ohate.survivalmultiplayer.module.chat.handler.MessageHandler;
import dev.ohate.survivalmultiplayer.util.Message;
import org.bukkit.entity.Player;

public class ReplyCommand {

    public ReplyCommand() {
        new CommandAPICommand("reply")
                .withAliases("r")
                .withArguments(new GreedyStringArgument("message"))
                .executesPlayer((sender, args) -> {
                    String message = (String) args.get(0);

                    MessageHandler handler = MessageHandler.getInstance();

                    if (!handler.hasLastMessaged(sender)) {
                        sender.sendMessage(Message.error("You have not messaged anyone."));
                        return;
                    }

                    Player receiver = handler.getLastMessaged(sender);

                    if (receiver == null || !receiver.isOnline()) {
                        sender.sendMessage(Message.error("That player is no longer online."));
                        return;
                    }

                    handler.sendMessage(sender, receiver, message);
                }).register();
    }

}
