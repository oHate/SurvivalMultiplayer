package dev.ohate.survivalmultiplayer.module.simple;

import dev.ohate.survivalmultiplayer.SurvivalMultiplayer;
import dev.ohate.survivalmultiplayer.framework.Framework;
import dev.ohate.survivalmultiplayer.framework.Module;
import dev.ohate.survivalmultiplayer.module.simple.listener.*;
import lombok.Getter;
import org.bukkit.event.Listener;

import java.util.List;

@Getter
public class SimpleModule extends Module {

    @Getter
    private static SimpleModule instance;

    public SimpleModule() {
        instance = this;
    }

    @Override
    public Framework getFramework() {
        return SurvivalMultiplayer.getInstance();
    }

    @Override
    public String getName() {
        return "Simple";
    }

    @Override
    public String getConfigFileName() {
        return "simple";
    }

    @Override
    public List<Listener> getListeners() {
        return List.of(
                new PlayerListener(),
                new CustomPortalListener(),
                new BottledExpListener(),
                new DragonEggListener(),
                new FastLeafDecayListener()
        );
    }

}
