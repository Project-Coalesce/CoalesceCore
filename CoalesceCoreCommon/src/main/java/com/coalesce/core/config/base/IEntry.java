package com.coalesce.core.config.base;

import java.util.ArrayList;
import java.util.List;

public interface IEntry {
	
	default <T> T getAs(Class<T> type) {
		return ((T)getValue());
	}
	
	/**
	 * Gets a value from a config entry.
	 *
	 * @return A string from the specified path.
	 */
	default String getString() {
		return getAs(String.class);
	}
	
	/**
	 * Gets a value from a config entry.
	 *
	 * @return A boolean from the specified path.
	 */
	default boolean getBoolean() {
		return getAs(Boolean.class);
	}
	
	/**
	 * Get the entry value as a type of list
	 * @param type The type of list to return
	 * @param <E> The list type desired
	 * @return The list
	 */
	default <E> List<E> getList(Class<E> type) {
		if (getList() == null) {
			return new ArrayList<>(0);
		}
		
		List<E> result = new ArrayList<>();
		
		for (Object object : getList()) {
			if (object instanceof String) {
				result.add((E)object);
			}
		}
		
		return result;
	}
	
	/**
	 * Gets a value from a config entry.
	 *
	 * @return A list from the specified path.
	 */
	default List<?> getList() {
		return getAs(List.class);
	}
	
	/**
	 * Gets a string list from an entry.
	 * @return The String list.
	 */
	default List<String> getStringList() {
		return getList(String.class);
	}
	
	/**
	 * Gets the path of this entry.
	 *
	 * @return The path in the config.
	 */
	String getPath();
	
	/**
	 * Gets the value of this entry.
	 *
	 * @return The value of this entry.
	 */
	Object getValue();
	
	/**
	 * Sets the path of this entry.
	 *
	 * @param newpath
	 *            The new path this entry will have.
	 */
	IEntry setPath(String newpath);
	
	/**
	 * Sets the value of this entry.
	 *
	 * @param value
	 *            The new value of this entry.
	 */
	IEntry setValue(Object value);
	
	/**
	 * Gets the database this entry is held in.
	 *
	 * @return The entry's database.
	 */
	IConfig getConfig();
	
	/**
	 * Gets the name of this entry.
	 * @return The entry name.
	 */
	String getName();
	
	/**
	 * Removes the entry.
	 */
	void remove();
	
}
