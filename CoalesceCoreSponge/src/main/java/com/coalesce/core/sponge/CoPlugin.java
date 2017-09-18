package com.coalesce.core.sponge;

import com.coalesce.core.plugin.ICoModule;
import com.coalesce.core.plugin.ICoPlugin;
import com.coalesce.core.session.SessionStore;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartingServerEvent;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;

import java.util.LinkedList;
import java.util.List;

public abstract class CoPlugin implements ICoPlugin {
	
	private final SessionStore sessionStore = new SessionStore();
	private final List<ICoModule> modules = new LinkedList<>();
	private static CoPlugin instance;
	
	@Listener
	public final void onEnable(GameStartingServerEvent e) {
		
		instance = this;
		com.coalesce.core.sponge.CoreSponge.addSessionStore(this, sessionStore);
		
		try {
			onPluginEnable();
		}
		catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	public final void onDisable(GameStoppingServerEvent e) {
		try {
			onPluginDisable();
		}
		catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	public final void onLoad(GameInitializationEvent e) {
		try {
			onPluginLoad();
		}
		catch (Exception e1) {
			e1.printStackTrace();
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
		return com.coalesce.core.sponge.CoreSponge.getSessionStores().get(plugin);
	}
	
	@Override
	public final void registerListener(Object listener) {
		Sponge.getEventManager().registerListeners(this, listener);
	}
	
	@Override
	public final void unregisterListener(Object listener) {
		Sponge.getEventManager().unregisterListeners(listener);
	}
}
