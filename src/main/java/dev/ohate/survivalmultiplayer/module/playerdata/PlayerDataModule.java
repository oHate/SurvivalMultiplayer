package dev.ohate.survivalmultiplayer.module.playerdata;

import dev.ohate.survivalmultiplayer.SurvivalMultiplayer;
import dev.ohate.survivalmultiplayer.framework.Framework;
import dev.ohate.survivalmultiplayer.framework.Handler;
import dev.ohate.survivalmultiplayer.framework.Module;
import dev.ohate.survivalmultiplayer.module.playerdata.handler.PlayerDataHandler;
import dev.ohate.survivalmultiplayer.module.playerdata.listener.PlayerDataListener;
import lombok.Getter;
import org.bukkit.event.Listener;

import java.util.List;

public class PlayerDataModule extends Module {

    @Getter
    private static PlayerDataModule instance;

    public PlayerDataModule() {
        instance = this;
    }

    @Override
    public Framework getFramework() {
        return SurvivalMultiplayer.getInstance();
    }

    @Override
    public String getName() {
        return "Player Data";
    }

    @Override
    public String getConfigFileName() {
        return "playerdata";
    }

    @Override
    public List<Handler> getHandlers() {
        return List.of(new PlayerDataHandler());
    }

    @Override
    public List<Listener> getListeners() {
        return List.of(new PlayerDataListener());
    }

}
