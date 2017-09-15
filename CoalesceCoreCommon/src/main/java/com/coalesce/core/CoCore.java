package com.coalesce.core;

import com.coalesce.core.plugin.ICoPlugin;
import com.coalesce.core.session.SessionStore;

/**
 * Provides static access to the entire api and its features.
 */
public final class CoCore {
	
	private static final SessionStore SESSION_STORE;
	
	static {
		SESSION_STORE = new SessionStore();
	}
	
	public SessionStore getSessionStore() {
		return SESSION_STORE;
	}
	
}
