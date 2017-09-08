package com.coalesce.core.session;

import com.coalesce.core.plugin.ICoPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class SessionStore {
	
	private static final Set<AbstractSession> MATCHED_SESSIONS;
	private static final Map<ICoPlugin, List<AbstractSession>> SESSIONS;
	
	static {
		MATCHED_SESSIONS = new HashSet<>();
		SESSIONS = new HashMap<>();
	}
	
	/**
	 * Gets a list of session pairs from a plugin.
	 * @param plugin The owning plugin
	 * @return A list of pairs
	 */
	public List<AbstractSession> getSessions(ICoPlugin plugin) {
		return SESSIONS.get(plugin);
	}
	
	/**
	 * Gets all the SESSIONS that have the specified key
	 * @param key The key to look for
	 * @return A set of SESSIONS
	 */
	public Set<AbstractSession> getSession(String key) {
		MATCHED_SESSIONS.clear();
		SESSIONS.forEach((p, l) -> l.forEach((s) -> {
				if (s.getSessionKey().equals(key)) MATCHED_SESSIONS.add(s);
			}));
		return MATCHED_SESSIONS;
	}
	
	/**
	 * Adds a session to a plugin
	 * @param session The session to add
	 * @param plugin The plugin that owns this session
	 * @return True if the session was added, false otherwise.
	 */
	public boolean addSession(AbstractSession session, ICoPlugin plugin) {
		if (!SESSIONS.containsKey(plugin)) {
			SESSIONS.put(plugin, new ArrayList<>());
		}
		if (SESSIONS.get(plugin).contains(session)) {
			return false;
		}
		else {
			SESSIONS.get(plugin).add(session);
			return true;
		}
	}
	
}
