package com.coalesce.core.session;

import com.coalesce.core.plugin.ICoPlugin;

import java.util.Map;

public interface ISession<T> {
    
    /**
     * Gets the session key for this session
     *
     * @return The session key
     */
    String getSessionKey();
    
    /**
     * Gets the name of the namespace this session is stored in.
     *
     * @return The session namespace name
     */
    String getNamespaceName();
    
    /**
     * The NamespacedSessionStore this Session is stored in
     *
     * @return The sessions NamespacedSessionStore
     */
    NamespacedSessionStore getNamespace();
    
    /**
     * The plugin that owns the session
     *
     * @return The session owner
     */
    ICoPlugin getSessionOwner();
    
    /**
     * Returns the session type
     *
     * @return The type of this session
     */
    T get();
    
    /**
     * Get the session meta map
     *
     * @return The session meta map
     */
    Map<?, ?> getSessionMeta();
    
    /**
     * Gets the type of session this is
     *
     * @return The session type
     */
    Class<? extends ISession> getSessionType();
    
}
