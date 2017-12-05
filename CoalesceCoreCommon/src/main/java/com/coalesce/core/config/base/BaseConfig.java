package com.coalesce.core.config.base;

import com.coalesce.core.plugin.ICoPlugin;

import java.util.Collection;
import java.util.HashSet;

public abstract class BaseConfig implements IConfig {
	
	private final String name;
	private final ICoPlugin plugin;
	protected final Collection<IEntry> entries;
	
	/**
	 * Creates a new configuration
	 * @param name The name/path of the configuration.
	 * @param plugin The plugin that owns this config.
	 */
	public BaseConfig(String name, ICoPlugin plugin) {
		this.entries = new HashSet<>();
		this.plugin = plugin;
		this.name = name;
	}
	
	@Override
	public ICoPlugin getPlugin() {
		return plugin;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public Collection<IEntry> getEntries() {
		return entries;
	}
}
