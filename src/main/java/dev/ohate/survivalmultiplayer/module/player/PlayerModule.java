package dev.ohate.survivalmultiplayer.module.player;

import dev.ohate.survivalmultiplayer.SurvivalMultiplayer;
import dev.ohate.survivalmultiplayer.framework.Framework;
import dev.ohate.survivalmultiplayer.framework.Module;
import dev.ohate.survivalmultiplayer.module.player.listener.PlayerListener;
import lombok.Getter;
import org.bukkit.event.Listener;

import java.util.List;

@Getter
public class PlayerModule extends Module {

    @Getter
    private static PlayerModule instance;

    public PlayerModule() {
        instance = this;
    }

    @Override
    public Framework getFramework() {
        return SurvivalMultiplayer.getInstance();
    }

    @Override
    public String getName() {
        return "Player";
    }

    @Override
    public String getConfigFileName() {
        return "player";
    }

    @Override
    public List<Listener> getListeners() {
        return List.of(new PlayerListener());
    }

}
