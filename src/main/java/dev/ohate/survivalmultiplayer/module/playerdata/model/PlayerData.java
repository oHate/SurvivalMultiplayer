package dev.ohate.survivalmultiplayer.module.playerdata.model;

import com.google.gson.annotations.SerializedName;
import com.mongodb.client.MongoCollection;
import dev.ohate.survivalmultiplayer.SurvivalMultiplayer;
import lombok.Data;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.bukkit.Material;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
public class PlayerData {

    @SerializedName("_id")
    private final ObjectId id;
    private final UUID uuid;
    private String username;

    private final Set<Material> filteredItems;

    public PlayerData(UUID uuid, String username) {
        this.id = new ObjectId();
        this.uuid = uuid;
        this.username = username;

        filteredItems = new HashSet<>();
    }

    public static MongoCollection<Document> getCollection() {
        return SurvivalMultiplayer.getInstance().getDatabase().getCollection("playerdata");
    }

    public boolean isItemFiltered(Material material) {
        return filteredItems.contains(material);
    }

}
