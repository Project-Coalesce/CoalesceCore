package com.coalesce.core.wrappers;

import com.coalesce.core.SenderType;
import com.coalesce.core.plugin.ICoPlugin;

public interface CoSender<T> {
	
	/**
	 * Sends the sender a message
	 * @param message The message to send
	 */
	void sendMessage(String message);
	
	/**
	 * Sends a series of messages to the sender
	 * @param messages The messages to send
	 */
	default void sendMessage(String... messages) {
		for (String message : messages) {
			sendMessage(message);
		}
	}
	
	/**
	 * Sends a formatted plugin message.
	 * @param message The message to send
	 */
	default void pluginMessage(String message) {
		sendMessage(getPlugin().getCoFormatter().format(message));
	}
	
	/**
	 * Sends a series of plugin messages to the sender
	 * @param messages The messages to send
	 */
	default void pluginMessage(String... messages) {
		for (String message : messages) {
			pluginMessage(message);
		}
	}
	
	/**
	 * Checks if the sender has any of the permissions specified
	 * @param permissions The permissions to look for.
	 */
	default boolean hasAnyPermission(String... permissions) {
		for (String permission : permissions) {
			if (hasPermission(permission)) return true;
		}
		return false;
	}
	
	/**
	 * Checks to see if the sender has all of the permissions specified
	 * @param permissions The permissions to look for.
	 */
	default boolean hasAllPermissions(String... permissions) {
		for (String permission : permissions) {
			if (!hasPermission(permission)) return false;
		}
		return true;
	}
	
	/**
	 * The name of the sender
	 * @return The name of the sender
	 */
	String getName();
	
	/**
	 * Gets the type of sender being used
	 * @return The {@link SenderType}
	 */
	SenderType getType();
	
	/**
	 * Checks if the sender has permission.
	 * @param permission The permission to look for
	 * @return True if the sender has permission.
	 */
	boolean hasPermission(String permission);
	
	/**
	 * The base player of the platform currently being used
	 * @return The platforms player class
	 */
	T getBase();
	
	/**
	 * Returns the sender as a specific type.
	 * @param type Type must extend the type of sender
	 * @return The base sender as a specific type.
	 *
	 * <p>
	 *     E.g. CoSender#as(Player.class); returns the sender as a player.
	 * </p>
	 */
	<E extends T> E as(Class<E> type);
	
	/**
	 * The owning plugin
	 * @return The plugin
	 */
	ICoPlugin getPlugin();
	
}
