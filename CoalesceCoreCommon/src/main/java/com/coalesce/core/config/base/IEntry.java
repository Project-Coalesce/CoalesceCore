package com.coalesce.core.config.base;

import java.util.ArrayList;
import java.util.List;

public interface IEntry {
	
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
	 * Gets the database this entry is held in.
	 *
	 * @return The entry's database.
	 */
	IConfig getConfig();
	
	/**
	 * Gets the entry value as a specific type
	 * @param type The type of value to return.
	 * @param <T> The type
	 * @return The value of the entry as the specified type.
	 */
	@SuppressWarnings("unchecked")
	default <T> T getAs(Class<T> type) {
		if (type.equals(Long.class)) return (T)Long.valueOf(getValue().toString());
		if (type.equals(Byte.class)) return (T)Byte.valueOf(getValue().toString());
		if (type.equals(Short.class)) return (T)Short.valueOf(getValue().toString());
		if (type.equals(Double.class)) return (T)Double.valueOf(getValue().toString());
		if (type.equals(Integer.class)) return (T)Integer.valueOf(getValue().toString());
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
	 * Checks if the value of this entry is a list.
	 * @return True if its a list.
	 */
	default boolean isList() {
		return getValue() instanceof List;
	}
	
	/**
	 * Checks if the value of this entry is a specific type
	 * @param type The class of this type
	 * @param <T> The type comparing the object to.
	 * @return True if the type of the object matches the Type
	 */
	default <T> boolean isType(Class<T> type) {
		return type.isInstance(getValue());
	}
	
	/**
	 * Gets a string list from an entry.
	 * @return The String list.
	 */
	default List<String> getStringList() {
		return getList(String.class);
	}
	
	/**
	 * Sets the path of this entry.
	 *
	 * @param newpath
	 *            The new path this entry will have.
	 */
	default IEntry setPath(String newpath) {
		remove();
		getConfig().setEntry(newpath, getValue());
		return this;
	}
	
	/**
	 * Sets the value of this entry.
	 *
	 * @param value
	 *            The new value of this entry.
	 */
	default IEntry setValue(Object value) {
		getConfig().setEntry(getPath(), value);
		return this;
	}
	
	/**
	 * Gets the name of this entry.
	 * @return The entry name.
	 */
	default String getName() {
		if (getPath().contains(".")) return getPath().substring(getPath().lastIndexOf(".") + 1);
		else return getPath();
	}
	
	/**
	 * Removes the entry.
	 */
	default void remove() {
		getConfig().setEntry(getPath(), null);
	}
	
}
