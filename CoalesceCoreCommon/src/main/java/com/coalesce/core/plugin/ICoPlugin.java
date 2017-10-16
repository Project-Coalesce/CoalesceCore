package com.coalesce.core.plugin;

import com.coalesce.core.Color;
import com.coalesce.core.Platform;
import com.coalesce.core.chat.CoFormatter;
import com.coalesce.core.command.base.CommandStore;
import com.coalesce.core.config.ConfigManager;
import com.coalesce.core.config.base.IConfig;
import com.coalesce.core.session.SessionStore;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public interface ICoPlugin {
	
	/**
	 * Method called when the plugin is enabled
	 */
	void onPluginEnable() throws Exception;
	
	/**
	 * Method called when the plugin is disabled
	 */
	void onPluginDisable() throws Exception;
	
	/**
	 * Method called when the plugin is loaded
	 */
	void onPluginLoad() throws Exception;
	
	/**
	 * Get the session store of the plugin
	 * @return The plugin session store.
	 */
	SessionStore getSessionStore();
	
	/**
	 * Get a set of modules the plugin has.
	 * @return This plugin's modules.
	 */
	List<ICoModule> getModules();
	
	/**
	 * Get the session store of another plugin
	 * @param plugin The plugin to get the session store from
	 * @return The plugins session store
	 */
	SessionStore getSessionStore(ICoPlugin plugin);
	
	/**
	 * Adds modules to this plugin
	 * @param modules The module(s) to add
	 */
	default void addModules(ICoModule... modules) {
		Collections.addAll(getModules(), modules);
	}
	
	/**
	 * Gets a module by class.
	 * @param clazz The class that extends CoModule
	 * @param <M> A module class
	 * @return The module.
	 */
	default <M extends ICoModule> M getModule(Class<M> clazz) {
		return getModules().stream().filter(module -> module.getClass().equals(clazz)).map(module -> ((M)module)).iterator().next();
	}
	
	/**
	 * Enables all this plugins modules.
	 */
	default void enableModules() {
		getModules().forEach(ICoModule::enable);
	}
	
	/**
	 * Disables all this plugins modules.
	 */
	default void disableModules() {
		getModules().forEach(ICoModule::disable);
	}
	
	/**
	 * Registers a listener class
	 * @param listener The listener to register
	 */
	void registerListener(Object listener);
	
	/**
	 * Unregisters a listener class
	 * @param listener The listener to unregister
	 */
	void unregisterListener(Object listener);
	
	/**
	 * Registers an array of listeners
	 * @param listeners The listeners to register
	 */
	default void registerListeners(Object... listeners) {
		Arrays.asList(listeners).forEach(this::registerListeners);
	}
	
	/**
	 * Sets the displayname of the plugin
	 * @param displayName The plugin displayname.
	 */
	void setDisplayName(String displayName);
	
	/**
	 * Gets the display name of the plugin.
	 * @return The plugin with the correct plugin color.
	 */
	String getDisplayName();
	
	/**
	 * Sets the color of this plugin
	 * @param color The plugin color.
	 */
	void setPluginColor(Color color);
	
	/**
	 * Gets the color of the plugin.
	 * @return Defaults to white if color has not been specified.
	 */
	Color getPluginColor();
	
	/**
	 * Gets the plugin logger
	 * @return The plugin logger.
	 */
	CoLogger getCoLogger();
	
	/**
	 * Gets the plugin formatter
	 * @return Plugin formatter.
	 */
	CoFormatter getCoFormatter();
	
	/**
	 * Gets the server platform this plugin is currently being ran on.
	 * @return The server platform.
	 */
	Platform getPlatform();
	
	/**
	 * Gets the command store
	 * @return The command store.
	 */
	CommandStore getCommandStore();
	
	/**
	 * Gets either the mods folder or the plugin folder depending on the platform.
	 * @return plugins folder for bukkit, mods folder for sponge.
	 */
	File getPluginFolder();
	
	/**
	 * Gets the plugins configuration manager.
	 * @return The plugins config manager.
	 */
	ConfigManager getConfigManager();
	
}
