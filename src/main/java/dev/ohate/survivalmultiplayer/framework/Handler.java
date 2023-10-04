package dev.ohate.survivalmultiplayer.framework;

import lombok.Data;

import java.io.File;

@Data
public abstract class Handler {

    private boolean loaded = false;

    public abstract Module getModule();

    public void initialLoad() {
    }

    public void saveData() {
        if (!loaded) {
            throw new IllegalStateException("Can't save a handler that hasn't been loaded");
        }
    }

    public void ensureResourceExists(File file) {
        file.getParentFile().mkdirs();
    }

}
