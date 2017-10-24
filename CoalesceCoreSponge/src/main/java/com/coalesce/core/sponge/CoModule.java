package com.coalesce.core.sponge;

import com.coalesce.core.plugin.ICoModule;
import com.coalesce.core.plugin.ICoPlugin;

public abstract class CoModule implements ICoModule {
	
	
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
			isEnabled = true;
			onPostEnable();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void disable() {
		if (!isEnabled) throw new IllegalStateException("Module " + getName() + " isn't enabled.");
		
		try {
			onDisable();
			isEnabled = false;
			onPostDisable();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void onPostEnable() throws Exception {}
	
	@Override
	public void onPostDisable() throws Exception {}
	
	public abstract void onEnable() throws Exception;
	
	public abstract void onDisable() throws Exception;
}
