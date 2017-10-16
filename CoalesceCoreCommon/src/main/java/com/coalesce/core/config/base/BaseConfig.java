package com.coalesce.core.config.base;

import com.coalesce.core.plugin.ICoPlugin;

import java.util.Collection;
import java.util.HashSet;

public abstract class BaseConfig implements IConfig {
	
	private final String name;
	private final ICoPlugin plugin;
	private final boolean memoryLoaded;
	protected final Collection<IEntry> entries;
	
	/**
	 * Creates a new configuration
	 * @param name The name/path of the configuration.
	 * @param plugin The plugin that owns this config.
	 *
	 *               <p>Note: this is automatically added to the config map if it doesn't exist already.</p>
	 */
	public BaseConfig(String name, ICoPlugin plugin) {
		this(name, plugin, true);
	}
	
	/**
	 * Creates a new configuration
	 * @param name The name/path of the configuration.
	 * @param plugin The plugin that owns this config.
	 * @param loadToConfigMap Whether to load this config into memory or not. (default is true)
	 */
	public BaseConfig(String name, ICoPlugin plugin, boolean loadToConfigMap) {
		this.memoryLoaded = loadToConfigMap;
		this.entries = new HashSet<>();
		this.plugin = plugin;
		this.name = name;
		
		if (loadToConfigMap) loadToMemory();
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
	
	@Override
	public boolean isInMemory() {
		return memoryLoaded;
	}
	
	@Override
	public void loadToMemory() {
		plugin.getConfigManager().loadConfig(this);
	}
	
	@Override
	public void unloadFromMemory() {
		plugin.getConfigManager().unloadConfig(this);
	}
}