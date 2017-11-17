package com.coalesce.core.config;

import com.coalesce.core.config.base.BaseConfig;
import com.coalesce.core.config.base.IConfig;
import com.coalesce.core.plugin.ICoPlugin;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
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
	 * A map of all the configurations in this plugin.
	 * @return A map of all the plugin configurations.
	 */
	public Map<String, IConfig> getConfigMap() {
		return configMap;
	}
	
	/**
	 * Gets a collection of all the plugin configurations.
	 * @return The plugin config
	 */
	public Collection<IConfig> getConfigurations() {
		return configMap.values();
	}
	
	/**
	 * Loads a configuration into the plugins configuration map
	 * @param config The config to load
	 * @return The configuration once its loaded.
	 */
	public <T extends BaseConfig> T loadConfig(T config) {
		if (configMap.containsKey(config.getName())) return (T)configMap.get(config.getName());
		else return (T)configMap.put(config.getName(), config);
	}
	
	/**
	 * Loads a configuration into the configuration map
	 * @param file The file location of this config.
	 * @param type The type of configuration this is.
	 * @param <T> The config type (must extend BaseConfig)
	 * @return The configuration
	 */
	public <T extends BaseConfig> T loadConfig(File file, Class<T> type) {
		return loadConfig(loadStaticConfig(file, type));
	}
	
	/**
	 * Loads a yml configuration into the configuration map
	 * @param file The file location of this configuration
	 * @return The config
	 */
	public YmlConfig loadYmlConfig(File file) {
		return loadConfig(file, YmlConfig.class);
	}
	
	/**
	 * Statically loads a yml configuration
	 * @param file The file to get the configuration from
	 * @return The configuration
	 */
	public YmlConfig loadStaticYmlConfig(File file) {
		return loadStaticConfig(file, YmlConfig.class);
	}
	
	/**
	 * Loads a configuration without putting it into plugin memory.
	 * @param file The file to load.
	 * @return The configuration if it exists.
	 */
	public <T extends BaseConfig> T loadStaticConfig(File file, Class<T> type) {
		T config = null;
		try {
			config = type.getConstructor(String.class, ICoPlugin.class, boolean.class).newInstance(file.toString(), plugin, false);
		}
		catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			e.printStackTrace();
		}
		return config;
	}
	
	/**
	 * Loads a new configuration instance
	 * @param config The configuration instance
	 * @param <T> The config type
	 * @return The instance
	 */
	public <T extends BaseConfig> T loadStaticConfig(T config) {
		return config;
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
