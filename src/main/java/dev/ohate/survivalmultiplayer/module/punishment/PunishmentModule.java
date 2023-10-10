package dev.ohate.survivalmultiplayer.module.punishment;

import dev.ohate.survivalmultiplayer.SurvivalMultiplayer;
import dev.ohate.survivalmultiplayer.framework.Framework;
import dev.ohate.survivalmultiplayer.framework.Module;
import lombok.Getter;

public class PunishmentModule extends Module {

    @Getter
    private static PunishmentModule instance;

    public PunishmentModule() {
        instance = this;
    }

    @Override
    public Framework getFramework() {
        return SurvivalMultiplayer.getInstance();
    }

    @Override
    public String getName() {
        return "Punishment";
    }

    @Override
    public String getConfigFileName() {
        return "punishment";
    }

}
