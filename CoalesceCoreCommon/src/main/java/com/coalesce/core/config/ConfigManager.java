package com.coalesce.core.config;

import com.coalesce.core.config.base.IConfig;
import com.coalesce.core.plugin.ICoPlugin;

import java.util.HashMap;
import java.util.Map;

public class ConfigManager {

	private final ICoPlugin plugin;
	private final Map<String, IConfig> configMap;
	
	public ConfigManager(ICoPlugin plugin) {
		this.configMap = new HashMap<>();
		this.plugin = plugin;
	}
	
	/**
	 * Loads a configuration into the plugins configuration map
	 * @param config The config to load
	 * @return The configuration once its loaded.
	 */
	public IConfig loadConfig(IConfig config) {
		return configMap.put(config.getName(), config);
	}
	
	/**
	 * Gets a configuration from the plugins configuration map
	 * @param name The name of the configuration
	 * @return The configuration if it exists
	 */
	public IConfig getConfig(String name) {
		return configMap.get(name);
	}
	
	/**
	 * Gets a configuration from the plugins configuration map.
	 * @param name The name of the configuration
	 * @param type The configuration class
	 * @param <T> The type of config is supposed to be returned.
	 * @return The config.
	 */
	public <T extends IConfig> T getConfig(String name, Class<T> type) {
		return (T)configMap.get(name);
	}
	
	/**
	 * Unloads a configuration from the plugins configuration map.
	 * @param name The config to remove.
	 */
	public void unloadConfig(String name) {
		configMap.remove(name);
	}
	
	/**
	 * Unloads a configuration from the plugins configuration map.
	 * @param config The config to remove.
	 */
	public void unloadConfig(IConfig config) {
		configMap.remove(config.getName());
	}
	
	/**
	 * Gets the plugin that owns this configuration manager.
	 * @return The plugin.
	 */
	public ICoPlugin getPlugin() {
		return plugin;
	}
}
