package dev.ohate.survivalmultiplayer.module.punishment.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PunishmentType {
    BAN(
            "ban",
            "banned",
            "unbanned"
    ),

    MUTE(
            "mute",
            "muted",
            "unmuted"
    );

    private final String readable;
    private final String context;
    private final String undoContext;

    public static PunishmentType fromString(String punishmentType) {
        for (PunishmentType type : values()) {
            if (type.name().equalsIgnoreCase(punishmentType)) {
                return type;
            }
        }

        return null;
    }

}
