package dev.ohate.survivalmultiplayer.module.itemfilter;

import dev.ohate.survivalmultiplayer.SurvivalMultiplayer;
import dev.ohate.survivalmultiplayer.framework.Framework;
import dev.ohate.survivalmultiplayer.framework.Module;
import dev.ohate.survivalmultiplayer.module.itemfilter.command.ItemFilterCommand;
import dev.ohate.survivalmultiplayer.module.itemfilter.listener.ItemFilterListener;
import org.bukkit.event.Listener;

import java.util.List;

public class ItemFilterModule extends Module {

    @Override
    public Framework getFramework() {
        return SurvivalMultiplayer.getInstance();
    }

    @Override
    public String getName() {
        return "Item Filter";
    }

    @Override
    public String getConfigFileName() {
        return "itemfilter";
    }

    @Override
    public List<Listener> getListeners() {
        return List.of(new ItemFilterListener());
    }

    @Override
    public List<Class<?>> getCommands() {
        return List.of(ItemFilterCommand.class);
    }

}
