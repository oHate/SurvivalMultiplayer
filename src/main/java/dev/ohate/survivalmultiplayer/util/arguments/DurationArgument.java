package dev.ohate.survivalmultiplayer.util.arguments;

import dev.jorel.commandapi.arguments.*;
import dev.ohate.commonlib.Duration;
import dev.ohate.survivalmultiplayer.util.Message;

public class DurationArgument extends CustomArgument<Long, String> {

    public DurationArgument(String name, boolean allowPermanent) {
        super(new StringArgument(name), info -> {
            Duration duration = Duration.fromString(info.input());

            if (duration.isPermanent() && !allowPermanent) {
                throw CustomArgument.CustomArgumentException.fromAdventureComponent(
                        Message.error("A permanent duration is not allowed in this command.")
                );
            }

            if (duration.isInvalid()) {
                throw CustomArgument.CustomArgumentException.fromAdventureComponent(
                        Message.error("You have provided an invalid duration.")
                );
            }

            return duration.getValue();
        });

        replaceSuggestions(ArgumentSuggestions.strings((info) -> new String[]{"1h", "12h", "1d", "7d", "14d", "30d", "perm"}));
    }

    public DurationArgument(String name) {
        this(name, true);
    }

}
