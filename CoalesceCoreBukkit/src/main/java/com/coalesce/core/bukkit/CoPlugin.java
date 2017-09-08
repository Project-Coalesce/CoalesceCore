package com.coalesce.core.bukkit;

import com.coalesce.core.plugin.ICoPlugin;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class CoPlugin extends JavaPlugin implements ICoPlugin, Listener {
	
	private static CoPlugin instance;
	
	@Override
	public final void onEnable() {
		instance = this;
		try {
			this.onPluginEnable();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public final void onDisable() {
		try {
			this.onPluginDisable();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public final void onLoad() {
		try {
			this.onPluginLoad();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public abstract void onPluginEnable() throws Exception;
	
	@Override
	public abstract void onPluginDisable() throws Exception;
	
	@Override
	public void onPluginLoad() throws Exception {
	
	}
	
	public static CoPlugin getInstance() {
		return instance;
	}
}
