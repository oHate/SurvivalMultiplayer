package dev.ohate.survivalmultiplayer;

import dev.ohate.survivalmultiplayer.framework.Framework;
import dev.ohate.survivalmultiplayer.framework.Module;
import dev.ohate.survivalmultiplayer.module.itemfilter.ItemFilterModule;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class SurvivalMultiplayer extends Framework {

    @Getter
    private static SurvivalMultiplayer instance;

    private final List<Module> enabledModules = new ArrayList<>();

    @Override
    public void onEnable() {
        instance = this;

        enabledModules.addAll(List.of(
                new ItemFilterModule()
        ));
    }

    @Override
    public List<Module> getModules() {
        return enabledModules;
    }

}