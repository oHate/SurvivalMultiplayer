package dev.ohate.survivalmultiplayer.module.punishment.model;

import com.google.gson.annotations.SerializedName;
import dev.ohate.commonlib.Duration;
import dev.ohate.commonlib.TimeUtil;
import dev.ohate.commonlib.UnicodeUtil;
import dev.ohate.survivalmultiplayer.cache.UUIDCache;
import dev.ohate.survivalmultiplayer.util.C;
import dev.ohate.survivalmultiplayer.util.Message;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Data
public class Punishment implements Comparable<Punishment> {

    private final PunishmentType type;
    private final UUID addedById;
    private final Instant addedAt;
    private final long duration;
    private final String addedReason;

    private UUID removedById;
    private Instant removedAt;
    private String removedReason;

    public Punishment(PunishmentType type, UUID addedById, Instant addedAt, long duration, String addedReason) {
        this.type = type;
        this.addedById = addedById;
        this.addedAt = addedAt;
        this.duration = duration;
        this.addedReason = addedReason;
    }

    public void remove(UUID removedById, Instant removedAt, String removedReason) {
        this.removedById = removedById;
        this.removedAt = removedAt;
        this.removedReason = removedReason;
    }

    public void expire(String reason) {
        remove(null, getAddedAt().plusSeconds(duration), reason);
    }

    public boolean isRemoved() {
        return removedAt != null;
    }

    public boolean isPermanent() {
        return duration == Duration.PERMANENT;
    }

    public boolean isActive() {
        return !isRemoved() && (isPermanent() || !hasExpired());
    }

    public boolean hasExpired() {
        return !isPermanent() && getAddedAt().plusSeconds(duration).isAfter(Instant.now());
    }

    public Instant getTimeRemaining() {
        return getAddedAt().plusSeconds(duration).minusMillis(System.currentTimeMillis());
    }

    public List<Component> getPunishmentComponents() {
        return List.of(
                Message.chatSeparator(),
                Component.textOfChildren(
                        Component.text("(!) ", C.FAIL, TextDecoration.BOLD),
                        Component.text((isPermanent() ? "Permanently" : "Temporarily") + " " + type.getContext(), C.FAIL),
                        Component.text(" (!)", C.FAIL, TextDecoration.BOLD)
                ),
                Component.empty(),
                Component.textOfChildren(
                        Component.text("Added on", NamedTextColor.GRAY),
                        Component.text(" " + UnicodeUtil.DOUBLE_ANGLE_RIGHT_ARROW + " ", NamedTextColor.DARK_GRAY, TextDecoration.BOLD),
                        Component.text(TimeUtil.instantToString(addedAt), C.FAIL)
                ),
                Component.textOfChildren(
                        Component.text("Reason", NamedTextColor.GRAY),
                        Component.text(" " + UnicodeUtil.DOUBLE_ANGLE_RIGHT_ARROW + " ", NamedTextColor.DARK_GRAY, TextDecoration.BOLD),
                        Component.text(addedReason, C.FAIL)
                ),
                Component.textOfChildren(
                        Component.text("Expires", NamedTextColor.GRAY),
                        Component.text(" " + UnicodeUtil.DOUBLE_ANGLE_RIGHT_ARROW + " ", NamedTextColor.DARK_GRAY, TextDecoration.BOLD),
                        Component.text(getReadableTimeRemaining(), C.FAIL)
                ),
                Message.chatSeparator()
        );
    }

    public String getReadableTimeRemaining() {
        if (isRemoved()) {
            return "Removed";
        }

        if (isPermanent()) {
            return "Permanent";
        }

        if (hasExpired()) {
            return "Expired";
        }

        return TimeUtil.instantToString(getTimeRemaining());
    }

    @Override
    public int compareTo(@NotNull Punishment punishment) {
        int isActiveComparison = Boolean.compare(punishment.isActive(), isActive());
        if (isActiveComparison != 0) {
            return isActiveComparison;
        }

        int typeComparison = getType().compareTo(punishment.getType());
        if (typeComparison != 0) {
            return typeComparison;
        }

        return getAddedAt().compareTo(punishment.getAddedAt());
    }

}
