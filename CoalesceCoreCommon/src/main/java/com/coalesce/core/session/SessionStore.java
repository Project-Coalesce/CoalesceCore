package com.coalesce.core.session;

import com.coalesce.core.plugin.ICoPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@SuppressWarnings({"unchecked", "WeakerAccess", "unused"})
public final class SessionStore {
	
	private final Map<String, NamespacedSessionStore<? extends AbstractSession>> namespaces;
	
	public SessionStore() {
		this.namespaces = new HashMap<>();
	}
	
	public List<String> getNamespaceNames() {
		return namespaces.values().stream().map(NamespacedSessionStore::getName).collect(Collectors.toList());
	}
	
	public List<String> getNamespaceNames(ICoPlugin plugin) {
		return plugin.getSessionStore().getNamespaces().stream().map(NamespacedSessionStore::getName).collect(Collectors.toList());
	}
	
	/**
	 * Gets all the namespaces that belong to this plugin.
	 * @return The plugin namespaces
	 */
	public List<NamespacedSessionStore<AbstractSession>> getNamespaces() {
		return new ArrayList(namespaces.values());
	}
	
	/**
	 * Gets a namespace via name
	 * @param name The name of the namespace
	 * @return The namespace
	 */
	public NamespacedSessionStore<AbstractSession> getNamespace(String name) {
		return (NamespacedSessionStore<AbstractSession>)namespaces.get(name);
	}
	
	/**
	 * Gets a namespace via name and SessionType
	 * @param name The name of the namespace
	 * @param sessionType The session type (Class)
	 * @param <T> The session. (Extends {@link AbstractSession})
	 * @return The namespace if it exists. null otherwise.
	 */
	public <T extends AbstractSession> NamespacedSessionStore<T> getNamespace(String name, Class<T> sessionType) {
		return (NamespacedSessionStore<T>)namespaces.get(name);
	}
	
	/**
	 * Gets a list of namespaces from a plugin.
	 * @param plugin The owning plugin
	 * @return A list of namespaces
	 */
	public List<NamespacedSessionStore<AbstractSession>> getNamespaces(ICoPlugin plugin) {
		return plugin.getSessionStore(plugin).getNamespaces();
	}
	
	/**
	 * Adds a NamespacedSession to the pluing SessionStore
	 * @param namespace The name of the namespace to add
	 * @return The created namespace
	 */
	public NamespacedSessionStore<AbstractSession> addNamespace(String namespace) {
		if (namespaces.containsKey(namespace)) return (NamespacedSessionStore<AbstractSession>)namespaces.get(namespace);
		NamespacedSessionStore<AbstractSession> ns = new NamespacedSessionStore(namespace, AbstractSession.class);
		namespaces.put(namespace, ns);
		return ns;
	}
	
	/**
	 * Adds a NamespacedSession to the plugin SessionStore
	 * @param namespace The name of the namespace to add
	 * @param sessionType The type of session the namespaced session will hold
	 * @param <T> The sessionType.
	 * @return The created namespaced session.
	 */
	public <T extends AbstractSession> NamespacedSessionStore<T> addNamespace(String namespace, Class<T> sessionType) {
		if (namespaces.containsKey(namespace)) return (NamespacedSessionStore<T>)namespaces.get(namespace);
		NamespacedSessionStore<T> ns = new NamespacedSessionStore(namespace, sessionType);
		namespaces.put(namespace, ns);
		return ns;
	}
	
	/**
	 * Removes a namespace from the plugin
	 * @param namespace The name of the namespace to remove
	 * @return True if the namespace was successfully removed, false otherwise.
	 */
	public boolean removeNamespace(String namespace) {
		if (!namespaces.containsKey(namespace)) return false;
		namespaces.remove(namespace);
		return true;
	}
	
	/**
	 * Removes all stored namespaces and their sessions that match the given predicate
	 * @param predicate The predicate needed to be matched
	 */
	public void removeNamespaces(Predicate<NamespacedSessionStore<? extends AbstractSession>> predicate) {
		namespaces.values().stream().filter(predicate).map(NamespacedSessionStore::getName).forEach(this::removeNamespace);
	}
}
