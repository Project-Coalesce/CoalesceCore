package com.coalesce.core.config.base;

import com.coalesce.core.plugin.ICoPlugin;

import java.util.Collection;
import java.util.HashSet;

public abstract class BaseConfig implements IConfig {
	
	private final String name;
	private final ICoPlugin plugin;
	private final boolean createNow;
	protected final Collection<IEntry> entries;
	
	/**
	 * Creates a new configuration
	 * @param name The name/path of the configuration.
	 * @param plugin The plugin that owns this config.
	 */
	public BaseConfig(String name, ICoPlugin plugin) {
		this(name, plugin, true);
	}
	
	/**
	 * Creates a new configuration
	 * @param name The name/path of the configuration
	 * @param plugin The plugin that owns this configuration
	 * @param createNow If true, the file will be created upon initializing this constructor, false wont create the file right away.
	 */
	public BaseConfig(String name, ICoPlugin plugin, boolean createNow) {
		this.entries = new HashSet<>();
		this.createNow = createNow;
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
