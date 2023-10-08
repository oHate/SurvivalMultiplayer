package dev.ohate.survivalmultiplayer.util;

import dev.ohate.commonlib.Colors;
import net.kyori.adventure.text.format.TextColor;

import java.awt.*;

public class C {

    public static final TextColor MAIN = C.wrap(Colors.Flat.PETERRIVER);
    public static final TextColor SUCCESS = C.wrap(Colors.Flat.EMERALD);
    public static final TextColor FAIL = C.wrap(Colors.Flat.ALIZARIN);

    public static TextColor wrap(Color color) {
        return TextColor.color(color.getRGB());
    }

}
