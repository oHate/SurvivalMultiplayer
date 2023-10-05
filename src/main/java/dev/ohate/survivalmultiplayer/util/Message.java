package dev.ohate.survivalmultiplayer.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;

public class Message {

    public static final JoinConfiguration SPACE_SEPARATOR = JoinConfiguration.separator(Component.space());
    public static final JoinConfiguration COMMA_SEPARATOR = JoinConfiguration.separator(Component.text(", ", NamedTextColor.GRAY));

    public static final String CURVED_RIGHT_ARROW = "\u27A5";

    public static final Style DEFAULT_STYLE = Style.style()
            .color(NamedTextColor.WHITE)
            .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
            .build();

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

    public static String repeat(String string, int times) {
        return (new String(new char[times])).replace("\u0000", string);
    }

    public static Component separator(int times) {
        return Component.text(repeat(" ", times), NamedTextColor.DARK_GRAY, TextDecoration.STRIKETHROUGH);
    }

    public static Component chatSeparator() {
        return separator(75);
    }

    public static Component menuSeparator() {
        return separator(50);
    }

    public static Component scoreboardSeparator() {
        return separator(32);
    }

}
