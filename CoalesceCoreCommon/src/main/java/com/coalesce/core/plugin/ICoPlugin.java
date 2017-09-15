package com.coalesce.core.plugin;

import com.coalesce.core.session.SessionStore;

import java.util.Set;

public interface ICoPlugin {
	
	void onPluginEnable() throws Exception;
	
	void onPluginDisable() throws Exception;
	
	void onPluginLoad() throws Exception;
	
	SessionStore getSessionStore();
	
	Set<ICoModule> getModules();
	
	SessionStore getSessionStore(ICoPlugin plugin);
	
}
