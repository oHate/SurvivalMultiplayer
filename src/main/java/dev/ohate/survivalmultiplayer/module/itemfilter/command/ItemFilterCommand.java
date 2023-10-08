package dev.ohate.survivalmultiplayer.module.itemfilter.command;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ItemStackArgument;
import dev.ohate.survivalmultiplayer.module.playerdata.handler.PlayerDataHandler;
import dev.ohate.survivalmultiplayer.module.playerdata.model.PlayerData;
import dev.ohate.survivalmultiplayer.util.C;
import dev.ohate.survivalmultiplayer.util.Message;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ItemFilterCommand {

    public ItemFilterCommand() {
        new CommandAPICommand("itemfilter")
                .withAliases("filter")
                .withSubcommands(add(), remove(), list())
                .register();
    }

    private CommandAPICommand add() {
        return new CommandAPICommand("add")
                .withArguments(new ItemStackArgument("material"))
                .executesPlayer((player, commandArguments) -> {
                    Material material = ((ItemStack) commandArguments.get(0)).getType();

                    PlayerDataHandler handler = PlayerDataHandler.getInstance();
                    PlayerData data = handler.getPlayerData().get(player.getUniqueId());

                    boolean added = data.getFilteredItems().add(material);

                    if (added) {
                        player.sendMessage(Message.message(
                                Component.translatable(material.translationKey(), C.SUCCESS),
                                Component.text(" has been added to your filtered items list.", NamedTextColor.GRAY)
                        ));
                    } else {
                        player.sendMessage(Message.message(
                                Component.translatable(material.translationKey(), C.FAIL),
                                Component.text(" was already added to your filtered items list.", NamedTextColor.GRAY)
                        ));
                    }

                    handler.savePlayerDataAsync(data);
                });
    }

    private CommandAPICommand remove() {
        return new CommandAPICommand("remove")
                .withArguments(new ItemStackArgument("material"))
                .executesPlayer((player, commandArguments) -> {
                    Material material = ((ItemStack) commandArguments.get(0)).getType();

                    PlayerDataHandler handler = PlayerDataHandler.getInstance();
                    PlayerData data = handler.getPlayerData().get(player.getUniqueId());

                    boolean removed = data.getFilteredItems().remove(material);

                    if (removed) {
                        player.sendMessage(Message.message(
                                Component.translatable(material.translationKey(), C.SUCCESS),
                                Component.text(" has been removed from your filtered items list.", NamedTextColor.GRAY)
                        ));
                    } else {
                        player.sendMessage(Message.message(
                                Component.translatable(material.translationKey(), C.FAIL),
                                Component.text(" wasn't added to your filtered items list.", NamedTextColor.GRAY)
                        ));
                    }

                    handler.savePlayerDataAsync(data);
                });
    }

    private CommandAPICommand list() {
        return new CommandAPICommand("list")
                .executesPlayer((player, commandArguments) -> {
                    PlayerDataHandler handler = PlayerDataHandler.getInstance();
                    PlayerData data = handler.getPlayerData().get(player.getUniqueId());

                    if (data.getFilteredItems().isEmpty()) {
                        player.sendMessage(Message.message(
                                Component.text("You aren't filtering any items.", NamedTextColor.GRAY)
                        ));

                        return;
                    }

                    List<Component> filterComponents = new ArrayList<>();

                    int index = 0;
                    for (Material material : data.getFilteredItems()) {
                        filterComponents.add(Component.translatable(
                                material.translationKey(),
                                index % 2 == 0 ? NamedTextColor.GRAY : NamedTextColor.DARK_GRAY
                        ));

                        index++;
                    }

                    player.sendMessage(Component.space());
                    player.sendMessage(Message.message(
                            "Filtered Items",
                            Component.join(Message.SPACE_SEPARATOR, filterComponents)
                    ));
                    player.sendMessage(Component.space());
                });
    }


}
