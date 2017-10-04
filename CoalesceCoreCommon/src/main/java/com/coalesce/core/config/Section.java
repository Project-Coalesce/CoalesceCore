package com.coalesce.core.config;

import com.coalesce.core.config.base.IConfig;
import com.coalesce.core.config.base.ISection;
import com.coalesce.core.plugin.ICoPlugin;

public final class Section implements ISection {
	
	private final ICoPlugin plugin;
	private final IConfig config;
	private final String path;
	
	public Section(String path, IConfig config, ICoPlugin plugin) {
		this.plugin = plugin;
		this.config = config;
		this.path = path;
	}
	
	@Override
	public String getCurrentPath() {
		return path;
	}
	
	@Override
	public IConfig getConfig() {
		return config;
	}
	
	@Override
	public ICoPlugin getPlugin() {
		return plugin;
	}
}
