package dev.ohate.survivalmultiplayer.module.punishment.command;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import dev.ohate.survivalmultiplayer.module.playerdata.model.PlayerData;
import dev.ohate.survivalmultiplayer.module.punishment.handler.PunishmentHandler;
import dev.ohate.survivalmultiplayer.module.punishment.model.PunishmentType;
import dev.ohate.survivalmultiplayer.util.arguments.DurationArgument;
import dev.ohate.survivalmultiplayer.util.arguments.ProfileArgument;

import java.util.concurrent.CompletableFuture;

public class BanCommand {

    public BanCommand() {
        new CommandAPICommand("ban")
                .withPermission("smp.command.ban")
                .withArguments(
                        new ProfileArgument("profile"),
                        new DurationArgument("duration"),
                        new GreedyStringArgument("reason")
                ).executes((sender, args) -> {
                    CompletableFuture<PlayerData> playerDataFuture = args.getUnchecked(0);

                    playerDataFuture.thenAccept(playerData -> {
                        PunishmentHandler.getInstance().addPunishment(
                                sender,
                                PunishmentType.BAN,
                                playerData,
                                args.getUnchecked(1),
                                args.getUnchecked(2)
                        );
                    });
                }).register();
    }

}
