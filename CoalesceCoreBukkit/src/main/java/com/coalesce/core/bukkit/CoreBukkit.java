package com.coalesce.core.bukkit;

import com.coalesce.core.Color;
import com.coalesce.core.command.TestCommand;
import com.coalesce.core.plugin.ICoPlugin;
import com.coalesce.core.session.SessionStore;

import java.util.HashMap;
import java.util.Map;

public class CoreBukkit extends CoPlugin {
	
	//Creates a map of all the session stores for each plugin
	private static Map<ICoPlugin, SessionStore> SESSION_STORES = new HashMap<>();
	
	@Override
	public void onPluginEnable() throws Exception {
		setDisplayName("CoalesceCore");
		setPluginColor(Color.YELLOW);
		getCommandStore().registerCommand(new TestCommand());
	}
	
	@Override
	public void onPluginDisable() throws Exception {
	
	}

	static void addSessionStore(ICoPlugin plugin, SessionStore sessionStore) {
		SESSION_STORES.put(plugin, sessionStore);
	}
	
	static Map<ICoPlugin, SessionStore> getSessionStores() {
		return SESSION_STORES;
	}
	
}

