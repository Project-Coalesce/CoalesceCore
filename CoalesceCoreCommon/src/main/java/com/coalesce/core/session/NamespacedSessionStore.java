package com.coalesce.core.session;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public final class NamespacedSessionStore<T extends ISession> {

    private final Map<String, T> sessions;
    private final String namespace;
    private final Class<T> type;

    /**
     * Creates a new namespaced session.
     *
     * @param namespace The name of this namespace
     */
    public NamespacedSessionStore(String namespace, Class<T> type) {
        this.sessions = new HashMap<>();
        this.namespace = namespace;
        this.type = type;
    }

    /**
     * Gets this NamespacedSessionStore's namespace
     *
     * @return The namespace
     */
    public String getName() {
        return namespace;
    }

    /**
     * Gets a session from this Namespace
     *
     * @param sessionKey The session key
     * @return The session if it exists, null otherwise.
     */
    public T getSession(String sessionKey) {
        return sessions.get(sessionKey);
    }

    /**
     * Gets all the sessions that belong to this namespace
     *
     * @return The plugin sessions
     */
    public List<T> getSessions() {
        return new ArrayList<>(sessions.values());
    }

    /**
     * Gets all sessions that match the given predicate
     *
     * @param predicate The predicate to match
     * @return The sessions
     */
    public List<T> getSessions(Predicate<T> predicate) {
        return sessions.values().stream().filter(predicate).collect(Collectors.toList());
    }

    /**
     * Gets the type of sessions that are put in this namespace.
     *
     * @return The type of session this namespace allows
     */
    public Class<T> get() {
        return type;
    }

    /**
     * Adds a session to this NamespacedSession
     *
     * @param session The session to add
     * @return Whether the session could be added or not.
     */
    public T addSession(T session) {
        sessions.put(session.getSessionKey(), session);
        return session;
    }

    /**
     * Removes a session from the NamespacedSession
     *
     * @param session The session to remove from this namespace
     * @return The removed session
     */
    public T removeSession(T session) {
        return sessions.remove(session.getSessionKey());
    }

    /**
     * Removes a session from the NamespacedSession
     *
     * @param sessionKey The sessionKey
     * @return The removed session
     */
    public T removeSession(String sessionKey) {
        return sessions.remove(sessionKey);
    }

    /**
     * Removes sessions which match the given predicate
     *
     * @param predicate The predicate the sessions need to pass
     */
    public void removeSessions(Predicate<T> predicate) {
        sessions.values().stream().filter(predicate).forEach(this::removeSession);
    }
}
