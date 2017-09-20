package com.coalesce.core.sponge;

import com.coalesce.core.Color;
import com.coalesce.core.Platform;
import com.coalesce.core.chat.CoFormatter;
import com.coalesce.core.plugin.CoLogger;
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
	
	private String displayName = Sponge.getPluginManager().fromInstance(this).get().getName();
	private final CoFormatter formatter = new CoFormatter(this);
	private final SessionStore sessionStore = new SessionStore();
	private final List<ICoModule> modules = new LinkedList<>();
	private final CoLogger logger = new CoLogger(this);
	private Color pluginColor = Color.WHITE;
	private static CoPlugin instance;
	
	@Listener
	public final void onEnable(GameStartingServerEvent e) {
		
		instance = this;
		CoreSponge.addSessionStore(this, sessionStore);
		
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
	
	//
	//
	
	@Override
	public String getDisplayName() {
		return displayName;
	}
	
	@Override
	public void setDisplayName(String displayName) {
		this.displayName = pluginColor + displayName + Color.RESET;
	}
	
	//
	//
	
	
	@Override
	public Color getPluginColor() {
		return pluginColor;
	}
	
	//
	//
	
	@Override
	public void setPluginColor(Color pluginColor) {
		this.pluginColor = pluginColor;
		this.displayName = pluginColor + displayName + Color.RESET;
	}
	
	//
	//
	
	public static CoPlugin getInstance() {
		return instance;
	}
	
	//
	//
	
	@Override
	public final SessionStore getSessionStore() {
		return sessionStore;
	}
	
	//
	//
	
	@Override
	public final List<ICoModule> getModules() {
		return modules;
	}
	
	//
	//
	
	@Override
	public final SessionStore getSessionStore(ICoPlugin plugin) {
		return CoreSponge.getSessionStores().get(plugin);
	}
	
	//
	//
	
	@Override
	public final void registerListener(Object listener) {
		Sponge.getEventManager().registerListeners(this, listener);
	}
	
	//
	//
	
	@Override
	public final void unregisterListener(Object listener) {
		Sponge.getEventManager().unregisterListeners(listener);
	}
	
	//
	//
	
	
	@Override
	public CoLogger getCoLogger() {
		return logger;
	}
	
	//
	//
	
	@Override
	public CoFormatter getCoFormatter() {
		return formatter;
	}
	
	//
	//
	
	
	@Override
	public Platform getPlatform() {
		return Platform.SPONGE;
	}
}
