package com.coalesce.core.plugin;

import com.coalesce.core.session.SessionStore;

import java.util.Arrays;
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
	
}
