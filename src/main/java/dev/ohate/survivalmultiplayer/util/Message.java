package dev.ohate.survivalmultiplayer.util;

import dev.ohate.commonlib.StringUtil;
import dev.ohate.commonlib.UnicodeUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;

public class Message {

    public static final JoinConfiguration SPACE_SEPARATOR = JoinConfiguration.separator(Component.space());
    public static final JoinConfiguration COMMA_SEPARATOR = JoinConfiguration.separator(Component.text(", ", NamedTextColor.GRAY));

    public static final Style DEFAULT_STYLE = Style.style()
            .color(NamedTextColor.WHITE)
            .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
            .build();

    public static Component error(String reason) {
        return message("Error", Component.text(reason, NamedTextColor.GRAY));
    }

    public static Component message(ComponentLike... components) {
        return Component.text().append(
                Component.text("Survival", C.MAIN, TextDecoration.BOLD),
                Component.text(" " + UnicodeUtil.CURVED_RIGHT_ARROW + " ", NamedTextColor.DARK_GRAY)
        ).append(components).build();
    }

    public static Component message(String prefix, ComponentLike... components) {
        return Component.text().append(
                Component.text(prefix, C.MAIN, TextDecoration.BOLD),
                Component.text(" " + UnicodeUtil.CURVED_RIGHT_ARROW + " ", NamedTextColor.DARK_GRAY)
        ).append(components).build();
    }

    public static Component separator(int times) {
        return Component.text(StringUtil.repeat(" ", times), NamedTextColor.DARK_GRAY, TextDecoration.STRIKETHROUGH);
    }

    public static Component chatSeparator() {
        return separator(70);
    }

    public static Component hoverSeparator() {
        return separator(32);
    }

}
