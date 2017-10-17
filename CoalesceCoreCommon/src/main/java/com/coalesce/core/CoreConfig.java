package com.coalesce.core;

import com.coalesce.core.config.YmlConfig;
import com.coalesce.core.plugin.ICoPlugin;

public final class CoreConfig extends YmlConfig {
	
	public CoreConfig(ICoPlugin plugin) {
		super("config", plugin);
		addEntry("debug", false);
		addEntry("logprocess", true);
	}
	
	public boolean isDebugEnabled() {
		return getBoolean("debug");
	}
}
