package com.coalesce.core.config.common;

import com.coalesce.core.config.base.IConfig;
import com.coalesce.core.config.base.ISection;
import com.coalesce.core.plugin.ICoPlugin;

public final class Section implements ISection {
    
    private final IConfig config;
    private final String path;

    public Section(String path, IConfig config) {
        this.config = config;
        this.path = path;
    }
    
    @Override
    public void addEntry(String path, Object value) {
        config.addEntry((getCurrentPath().isEmpty() ? path : getCurrentPath() + "." + path), value);
    }
    
    @Override
    public void setEntry(String path, Object value) {
        config.setEntry((getCurrentPath().isEmpty() ? path : getCurrentPath() + "." + path), value);
    }
    
    @Override
    public String getCurrentPath() {
        return path;
    }

    @Override
    public IConfig getConfig() {
        return config;
    }
}
