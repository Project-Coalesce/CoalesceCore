package com.coalesce.core.bukkit;

import com.coalesce.core.plugin.ICoModule;
import com.coalesce.core.plugin.ICoPlugin;
import org.bukkit.event.Listener;

public abstract class CoModule implements Listener, ICoModule {
	
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

	public boolean isEnabled() {
		return isEnabled;
	}
	
	public String getName() {
		return name;
	}
	
	public ICoPlugin getPlugin() {
		return plugin;
	}
	
	public void enable() {
		if (isEnabled) throw new IllegalStateException("Module " + getName() + " is already disabled.");
		
		try {
			onEnable();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		isEnabled = true;
	}
	
	public void disable() {
		if (!isEnabled) throw new IllegalStateException("Module " + getName() + " isn't enabled.");
		
		try {
			onDisable();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		isEnabled = false;
	}
	
	public abstract void onEnable() throws Exception;
	
	public abstract void onDisable() throws Exception;
	
}
