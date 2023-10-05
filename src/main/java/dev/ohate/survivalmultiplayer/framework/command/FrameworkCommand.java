package dev.ohate.survivalmultiplayer.framework.command;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandPermission;
import dev.ohate.survivalmultiplayer.framework.Framework;
import dev.ohate.survivalmultiplayer.framework.Handler;
import dev.ohate.survivalmultiplayer.framework.Module;
import dev.ohate.survivalmultiplayer.util.Colors;
import dev.ohate.survivalmultiplayer.util.Message;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class FrameworkCommand {

    public FrameworkCommand() {
        new CommandAPICommand("framework")
                .withPermission(CommandPermission.OP)
                .executes((sender, args) -> {
                    for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
                        if (!(plugin instanceof Framework framework)) {
                            continue;
                        }

                        sender.sendMessage(Component.space());

                        sender.sendMessage(Component.textOfChildren(
                                Message.message("Framework"),
                                Component.text("Found ", NamedTextColor.GRAY),
                                Component.text(framework.getModules().size(), Colors.CYAN),
                                Component.text(" modules from ", NamedTextColor.GRAY),
                                Component.text(framework.getName(), Colors.CYAN)
                        ));

                        for (Module module : framework.getModules()) {
                            List<Component> toolTip = new ArrayList<>(List.of(
                                    Message.scoreboardSeparator(),
                                    Component.textOfChildren(
                                            Component.text("Commands: ", Colors.CYAN),
                                            Component.text(module.getCommands().size(), NamedTextColor.GRAY)
                                    ),
                                    Component.textOfChildren(
                                            Component.text("Handler Count: ", Colors.CYAN),
                                            Component.text(module.getCachedHandlers().size(), NamedTextColor.GRAY))
                            ));

                            for (Handler handler : module.getCachedHandlers()) {
                                toolTip.add(Component.textOfChildren(
                                        Component.text(" " + Message.CURVED_RIGHT_ARROW + " ", NamedTextColor.DARK_GRAY),
                                        Component.text(handler.getClass().getSimpleName(), Colors.CYAN)
                                ));
                            }

                            toolTip.add(Message.scoreboardSeparator());

                            Component moduleMessage = Component.textOfChildren(
                                    Component.text(" " + Message.CURVED_RIGHT_ARROW + " ", NamedTextColor.DARK_GRAY),
                                    Component.text(module.getName(), NamedTextColor.GRAY)
                            ).hoverEvent(HoverEvent.showText(Component.join(JoinConfiguration.newlines(), toolTip)));

                            sender.sendMessage(moduleMessage);
                        }

                    }

                    sender.sendMessage(Component.space());
                }).register();
    }

}
