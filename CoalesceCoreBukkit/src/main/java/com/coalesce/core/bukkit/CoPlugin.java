package com.coalesce.core.bukkit;

import com.coalesce.core.plugin.ICoModule;
import com.coalesce.core.plugin.ICoPlugin;
import com.coalesce.core.session.SessionStore;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Set;

public abstract class CoPlugin extends JavaPlugin implements ICoPlugin, Listener {
	
	private SessionStore sessionStore;
	private static CoPlugin instance;
	
	@Override
	public final void onEnable() {
		
		instance = this;
		this.sessionStore = new SessionStore();
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
	public Set<ICoModule> getModules() {
		return null;
	}
}
