package com.coalesce.core.session;

import com.coalesce.core.plugin.ICoPlugin;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractSession<T> implements ISession<T> {

    private final T type;
    private final String sessionKey;
    private final Map<?, ?> sessionMeta;
    private final ICoPlugin sessionOwner;
    private final NamespacedSessionStore<? extends ISession> namespace;
    private final Class<? extends ISession> session;

    public AbstractSession(ICoPlugin sessionOwner, NamespacedSessionStore<? extends ISession> namespace, String sessionKey, T type) {
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

    @Override
    public String getSessionKey() {
        return sessionKey;
    }

    @Override
    public String getNamespaceName() {
        return getNamespace().getName();
    }

    @Override
    public NamespacedSessionStore getNamespace() {
        return namespace;
    }

    @Override
    public ICoPlugin getSessionOwner() {
        return sessionOwner;
    }

    @Override
    public T get() {
        return type;
    }

    @Override
    public Map<?, ?> getSessionMeta() {
        return sessionMeta;
    }

    @Override
    public Class<? extends ISession> getSessionType() {
        return session;
    }

}
