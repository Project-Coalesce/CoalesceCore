package com.coalesce.core.plugin;

public abstract class CoModule {

	private final ICoPlugin plugin;
	private final String name;
	
	private boolean isEnabled;
	
	/**
	 * Creates a new module.
	 * @param plugin
	 * @param name
	 */
	public CoModule(ICoPlugin plugin, String name) {
		this.plugin = plugin;
		this.name = name;
	}
	
	/**
	 * Checks if this module is enabled.
	 * @return True if enabled
	 */
	public boolean isEnabled() {
		return isEnabled;
	}
	
	/**
	 * Gets the name of this module.
	 * @return The name of this module.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Gets the plugin this module belongs to.
	 * @return The owning plugin.
	 */
	public ICoPlugin getPlugin() {
		return plugin;
	}
	
}
