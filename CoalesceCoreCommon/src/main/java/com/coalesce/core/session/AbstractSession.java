package com.coalesce.core.session;

import com.coalesce.core.plugin.ICoPlugin;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractSession<T> {
	
	private final T type;
	private final String sessionKey;
	private final Map<?, ?> sessionMeta;
	private final ICoPlugin sessionOwner;
	private final Class<? extends AbstractSession> session;
	
	public AbstractSession(ICoPlugin sessionOwner, String sessionKey, T type) {
		this.type = type;
		this.sessionKey = sessionKey;
		this.session = this.getClass();
		this.sessionOwner = sessionOwner;
		this.sessionMeta = new HashMap<>();
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
	
	public Map<?, ?> getSessionMeta() {
		return sessionMeta;
	}
	
	public Class<? extends AbstractSession> getSessionType() {
		return session;
	}
	
}
