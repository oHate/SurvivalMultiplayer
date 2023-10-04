package dev.ohate.survivalmultiplayer.module.playerdata.model;

import lombok.Data;
import org.bukkit.Material;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
public class PlayerData {

    private final UUID uuid;
    private String username;

    private final Set<Material> filteredItems;

    public PlayerData(UUID uuid, String username) {
        this.uuid = uuid;
        this.username = username;

        filteredItems = new HashSet<>();
    }

    public boolean isItemFiltered(Material material) {
        return filteredItems.contains(material);
    }

}
