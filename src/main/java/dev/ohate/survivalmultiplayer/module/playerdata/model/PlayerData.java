package dev.ohate.survivalmultiplayer.module.playerdata.model;

import com.google.gson.annotations.SerializedName;
import com.mongodb.client.MongoCollection;
import dev.ohate.survivalmultiplayer.SurvivalMultiplayer;
import dev.ohate.survivalmultiplayer.module.punishment.model.Punishment;
import dev.ohate.survivalmultiplayer.module.punishment.model.PunishmentType;
import lombok.Data;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.bukkit.Material;

import java.util.*;

@Data
public class PlayerData {

    @SerializedName("_id")
    private final ObjectId id;
    private final UUID uuid;
    private String username;

    private final List<Punishment> punishments;
    private final Set<Material> filteredItems;

    public PlayerData(UUID uuid, String username) {
        this.id = new ObjectId();
        this.uuid = uuid;
        this.username = username;

        punishments = new ArrayList<>();
        filteredItems = new HashSet<>();
    }

    public static MongoCollection<Document> getCollection() {
        return SurvivalMultiplayer.getInstance().getDatabase().getCollection("playerdata");
    }

    public List<Punishment> getPunishmentsByType(PunishmentType type) {
        List<Punishment> punishments = new ArrayList<>(this.punishments);

        punishments.removeIf(punishment -> punishment.getType() != type);
        punishments.sort(Punishment::compareTo);

        return punishments;
    }

    public Punishment getActivePunishmentByType(PunishmentType type) {
        List<Punishment> punishments = getPunishmentsByType(type);

        if (punishments.isEmpty()) {
            return null;
        }

        Punishment punishment = punishments.get(0);

        if (!punishment.isActive()) {
            return null;
        }

        return punishment;
    }

    public boolean isItemFiltered(Material material) {
        return filteredItems.contains(material);
    }

}
