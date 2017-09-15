package com.coalesce.core.session;

import com.coalesce.core.plugin.ICoPlugin;

import java.util.ArrayList;
import java.util.List;

public final class SessionStore {
	
	private final List<AbstractSession> sessions;
	
	public SessionStore() {
		sessions = new ArrayList<>();
	}
	
	/**
	 * Gets all the sessions that belong to this plugin.
	 * @return THe plugin sessions
	 */
	public List<AbstractSession> getSessions() {
		return sessions;
	}
	
	/**
	 * Gets all the sessions that are a specific session type.
	 * @param sessionType The type of session this is.
	 * @param <T> The type of session to look for and return as
	 * @return Returns a list of sessions that are the correct type.
	 */
	public <T extends AbstractSession> List<? extends AbstractSession> getSessions(Class<T> sessionType) {
		List<T> correctSessions = new ArrayList<>();
		sessions.forEach(session -> {
			if (session.getSessionType().equals(sessionType)) correctSessions.add((T)session);
		});
		return correctSessions;
	}
	
	/**
	 * Gets a session by key. Plugin is not needed with this because it is automatically assumed its the current plugin.
	 * @param sessionType The session type.
	 * @param key The session key
	 * @param <T> The type of session to return.
	 * @return The session.
	 */
	public <T extends AbstractSession> T getSession(Class<T> sessionType, String key) {
		T correctSession = null;
		for (AbstractSession session : getSessions(sessionType)) {
			if (session.getSessionKey().matches(key)) {
				correctSession = (T)session;
				break;
			}
		}
		return correctSession;
	}
	
	/**
	 * Gets a list of session pairs from a plugin.
	 * @param plugin The owning plugin
	 * @return A list of pairs
	 */
	public List<AbstractSession> getSessions(ICoPlugin plugin) {
		return plugin.getSessionStore(plugin).getSessions();
	}
	
	public <T extends AbstractSession> List<? extends AbstractSession> getSessions(ICoPlugin plugin, Class<T> sessionType) {
		List<T> correctSessions = new ArrayList<>();
		getSessions(plugin).forEach(session -> {
			if (session.getSessionType().equals(sessionType)) correctSessions.add((T)session);
		});
		return correctSessions;
	}
	
	/**
	 * Gets a session from another plugin
	 * @param plugin The plugin to get the session from
	 * @param sessionType The type of session being retrieved
	 * @param key The session key
	 * @param <T> The type of session to return
	 * @return A session from another plugin
	 */
	public <T extends AbstractSession> T getSession(ICoPlugin plugin, Class<T> sessionType, String key) {
		T correctSession = null;
		for (AbstractSession session : getSessions(plugin, sessionType)) {
			if (session.getSessionKey().matches(key)) {
				correctSession = (T)session;
				break;
			}
		}
		return correctSession;
	}
	
	/**
	 * Adds a session to a plugin
	 * @param session The session to add
	 * @return True if the session was added, false otherwise.
	 */
	public boolean addSession(AbstractSession session) {
		return sessions.add(session);
	}
	
}
