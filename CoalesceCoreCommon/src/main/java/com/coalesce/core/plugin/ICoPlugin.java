package com.coalesce.core.plugin;

public interface ICoPlugin {
	
	void onPluginEnable() throws Exception;
	
	void onPluginDisable() throws Exception;
	
	void onPluginLoad() throws Exception;
	
}
