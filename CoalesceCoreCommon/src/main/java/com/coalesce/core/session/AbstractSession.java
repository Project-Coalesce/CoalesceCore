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
	
	/**
	 * Gets the session key for this session
	 * @return The session key
	 */
	public String getSessionKey() {
		return sessionKey;
	}
	
	/**
	 * The plugin that owns the session
	 * @return The session owner
	 */
	public ICoPlugin getSessionOwner() {
		return sessionOwner;
	}
	
	/**
	 * Returns the session type
	 * @return The type of this session
	 */
	public T get() {
		return type;
	}
	
	/**
	 * Get the session meta map
	 * @return The session meta map
	 */
	public Map<?, ?> getSessionMeta() {
		return sessionMeta;
	}
	
	/**
	 * Gets the type of session this is
	 * @return The session type
	 */
	public Class<? extends AbstractSession> getSessionType() {
		return session;
	}
	
}
