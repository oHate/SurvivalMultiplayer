package dev.ohate.survivalmultiplayer.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class Message {

    public static final JoinConfiguration SPACE_SEPARATOR = JoinConfiguration.separator(Component.space());
    public static final String CURVED_RIGHT_ARROW = "\u27A5";

    public static Component message(ComponentLike... components) {
        return Component.text().append(
                Component.text("Survival", Colors.CYAN, TextDecoration.BOLD),
                Component.text(" " + CURVED_RIGHT_ARROW + " ", NamedTextColor.DARK_GRAY)
        ).append(components).build();
    }

    public static Component message(String prefix, ComponentLike... components) {
        return Component.text().append(
                Component.text(prefix, Colors.CYAN, TextDecoration.BOLD),
                Component.text(" " + CURVED_RIGHT_ARROW + " ", NamedTextColor.DARK_GRAY)
        ).append(components).build();
    }

}
