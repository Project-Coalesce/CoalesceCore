package com.coalesce.core.config.common;

import com.coalesce.core.config.base.IConfig;
import com.coalesce.core.config.base.IEntry;

public final class Entry implements IEntry {
    
    private String key;
    private Object value;
    private final IConfig config;
    
    public Entry(IConfig config, String key) {
        this.value = config.getValue(key);
        this.config = config;
        this.key = key;
    }
    
    public Entry(IConfig config, String key, Object value) {
        this.config = config;
        this.value = value;
        this.key = key;
    }
    
    @Override
    public String getPath() {
        return key;
    }
    
    @Override
    public Object getValue() {
        return value;
    }
    
    @Override
    public IConfig getConfig() {
        return config;
    }
}
