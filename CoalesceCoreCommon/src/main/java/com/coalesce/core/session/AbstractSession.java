package com.coalesce.core.session;

import com.coalesce.core.plugin.ICoPlugin;

public abstract class AbstractSession<T> {
	
	private final T type;
	private final String sessionKey;
	private final ICoPlugin sessionOwner;
	
	public AbstractSession(ICoPlugin sessionOwner, String sessionKey, T type) {
		this.type = type;
		this.sessionKey = sessionKey;
		this.sessionOwner = sessionOwner;
	}
	
	public String getSessionKey() {
		return sessionKey;
	}
	
	public ICoPlugin getSessionOwner() {
		return sessionOwner;
	}
	
	public T get() {
		return type;
	}

}
