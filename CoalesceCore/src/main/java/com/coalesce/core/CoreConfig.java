package com.coalesce.core;

import com.coalesce.core.config.YmlConfig;

public final class CoreConfig extends YmlConfig {

    public CoreConfig(Core plugin) {
        super("config", plugin);
        addEntry("debug", false);
        addEntry("locale.overridePluginLocales", true);
        addEntry("locale.masterLocale", "en_us");
    }

    public boolean isDebugEnabled() {
        return getBoolean("debug");
    }
    
    public boolean overridePluginLocales() {
        return getBoolean("locale.overridePluginLocales");
    }
    
    public String getMasterLocale() {
        return getString("locale.masterLocale");
    }
    
}
