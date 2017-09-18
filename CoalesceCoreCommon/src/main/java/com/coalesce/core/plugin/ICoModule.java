package com.coalesce.core.plugin;

public interface ICoModule {
	
	/**
	 * Checks if this module is enabled.
	 * @return True if enabled
	 */
	boolean isEnabled();
	
	/**
	 * Gets the name of this module.
	 * @return The name of this module.
	 */
	String getName();
	
	/**
	 * Gets the plugin this module belongs to.
	 * @return The owning plugin.
	 */
	ICoPlugin getPlugin();
	
	/**
	 * Enables the module
	 */
	void enable();
	
	/**
	 * Disables the module
	 */
	void disable();
	
	/**
	 * Method called when a module is enabled
	 */
	void onEnable() throws Exception;
	
	/**
	 * Method called when a module is disabled
	 */
	void onDisable() throws Exception;
	
}
