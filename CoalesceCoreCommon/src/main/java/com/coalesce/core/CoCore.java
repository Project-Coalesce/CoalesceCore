package com.coalesce.core;

import com.coalesce.core.plugin.ICoPlugin;

/**
 * Provides static access to the entire api and its features.
 */
public final class CoCore {
	
	private static ICoPlugin plugin;
	
	public static void setOwner(ICoPlugin plugin) {
		if (CoCore.plugin == null) {
			CoCore.plugin = plugin;
		}
		else throw new UnsupportedOperationException("Cannot set a");
	}
	
	
	
}
