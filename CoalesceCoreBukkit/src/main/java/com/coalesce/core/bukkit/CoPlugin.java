package com.coalesce.core.bukkit;

import com.coalesce.core.plugin.ICoModule;
import com.coalesce.core.plugin.ICoPlugin;
import com.coalesce.core.session.SessionStore;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.LinkedList;
import java.util.List;

public abstract class CoPlugin extends JavaPlugin implements ICoPlugin, Listener {
	
	private final SessionStore sessionStore = new SessionStore();
	private final List<ICoModule> modules = new LinkedList<>();
	private static CoPlugin instance;
	
	@Override
	public final void onEnable() {
		
		instance = this;
		CoreBukkit.addSessionStore(this, sessionStore);
		
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
	
	@Override
	public final SessionStore getSessionStore() {
		return sessionStore;
	}
	
	@Override
	public final List<ICoModule> getModules() {
		return modules;
	}
	
	//Implements access to all plugin's session stores.
	@Override
	public final SessionStore getSessionStore(ICoPlugin plugin) {
		return CoreBukkit.getSessionStores().get(plugin);
	}
	
	@Override
	public final void registerListener(Object listener) {
		Bukkit.getPluginManager().registerEvents((Listener)listener, this);
	}
	
	@Override
	public final void unregisterListener(Object listener) {
		HandlerList.unregisterAll((Listener)listener);
	}
}
