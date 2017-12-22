package com.coalesce.core.session;

import com.coalesce.core.plugin.ICoPlugin;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractSession<T> {

    private final T type;
    private final String sessionKey;
    private final Map<?, ?> sessionMeta;
    private final ICoPlugin sessionOwner;
    private final NamespacedSessionStore<AbstractSession> namespace;
    private final Class<? extends AbstractSession> session;

    public AbstractSession(ICoPlugin sessionOwner, NamespacedSessionStore<AbstractSession> namespace, String sessionKey, T type) {
        this.type = type;
        this.namespace = namespace;
        this.sessionKey = sessionKey;
        this.session = this.getClass();
        this.sessionOwner = sessionOwner;
        this.sessionMeta = new HashMap<>();
    }

    public AbstractSession(ICoPlugin sessionOwner, String namespace, String sessionKey, T type) {
        this(sessionOwner, sessionOwner.getSessionStore().getNamespace(namespace), sessionKey, type);
    }

    /**
     * Gets the session key for this session
     *
     * @return The session key
     */
    public String getSessionKey() {
        return sessionKey;
    }

    /**
     * Gets the name of the namespace this session is stored in.
     *
     * @return The session namespace name
     */
    public String getNamespaceName() {
        return getNamespace().getName();
    }

    /**
     * The NamespacedSessionStore this Session is stored in
     *
     * @return The sessions NamespacedSessionStore
     */
    public NamespacedSessionStore getNamespace() {
        return namespace;
    }

    /**
     * The plugin that owns the session
     *
     * @return The session owner
     */
    public ICoPlugin getSessionOwner() {
        return sessionOwner;
    }

    /**
     * Returns the session type
     *
     * @return The type of this session
     */
    public T get() {
        return type;
    }

    /**
     * Get the session meta map
     *
     * @return The session meta map
     */
    public Map<?, ?> getSessionMeta() {
        return sessionMeta;
    }

    /**
     * Gets the type of session this is
     *
     * @return The session type
     */
    public Class<? extends AbstractSession> getSessionType() {
        return session;
    }

}
