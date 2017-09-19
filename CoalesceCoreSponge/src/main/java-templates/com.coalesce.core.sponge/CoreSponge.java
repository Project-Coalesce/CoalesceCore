package com.coalesce.core.sponge;

import com.coalesce.core.plugin.ICoPlugin;
import com.coalesce.core.session.SessionStore;
import org.spongepowered.api.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

@Plugin(
		authors = {"NJDaeger"},
		description = "Sponge implementation of CoalesceCore",
		id = "coalescecore",
		name = "${project.artifactId}",
		url = "https://github.com/Project-Coalesce/CoalesceCore",
		version = "${project.version}"
)
public class CoreSponge extends CoPlugin {
	
	//Creates a map of all the session stores for each plugin
	private static Map<ICoPlugin, SessionStore> SESSION_STORES = new HashMap<>();
	
	@Override
	public void onPluginEnable() throws Exception {

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
