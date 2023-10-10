package dev.ohate.survivalmultiplayer.module.punishment.handler;

import dev.ohate.commonlib.Duration;
import dev.ohate.survivalmultiplayer.framework.Handler;
import dev.ohate.survivalmultiplayer.framework.Module;
import dev.ohate.survivalmultiplayer.module.playerdata.handler.PlayerDataHandler;
import dev.ohate.survivalmultiplayer.module.playerdata.model.PlayerData;
import dev.ohate.survivalmultiplayer.module.punishment.PunishmentModule;
import dev.ohate.survivalmultiplayer.module.punishment.model.Punishment;
import dev.ohate.survivalmultiplayer.module.punishment.model.PunishmentType;
import dev.ohate.survivalmultiplayer.util.Message;
import dev.ohate.survivalmultiplayer.util.arguments.ProfileArgument;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.Instant;

public class PunishmentHandler extends Handler {

    @Getter
    private static PunishmentHandler instance;

    public PunishmentHandler() {
        instance = this;
    }

    public void addPunishment(CommandSender sender, PunishmentType type, PlayerData targetPlayerData, Duration duration, String reason) {
        if (targetPlayerData == null) {
            sender.sendMessage(ProfileArgument.ERROR);
            return;
        }

        if (targetPlayerData.getActivePunishmentByType(type) != null) {
            sender.sendMessage(Message.error("The provided player already has an active " + type.getReadable() + "."));
            return;
        }

        if (!duration.isInvalid()) {
            sender.sendMessage(Message.error("The provided duration is not valid."));
            return;
        }

        Punishment punishment = new Punishment(
                type,
                sender instanceof Player player ? player.getUniqueId() : null,
                Instant.now(),
                duration.getValue(),
                reason
        );

        targetPlayerData.getPunishments().add(punishment);

        PlayerDataHandler.getInstance().savePlayerDataAsync(targetPlayerData);

        Player targetPlayer = Bukkit.getPlayer(targetPlayerData.getUuid());

        if (targetPlayer == null || !targetPlayer.isOnline()) {
            return;
        }

        if (type == PunishmentType.BAN) {
            targetPlayer.kick(Component.join(JoinConfiguration.newlines(), punishment.getPunishmentComponents()));
        } else if (type == PunishmentType.MUTE) {
            for (Component component : punishment.getPunishmentComponents()) {
                targetPlayer.sendMessage(component);
            }
        }
    }

    public void removePunishment() {

    }

    @Override
    public Module getModule() {
        return PunishmentModule.getInstance();
    }

}
