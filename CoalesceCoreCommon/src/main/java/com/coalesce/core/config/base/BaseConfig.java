package com.coalesce.core.config.base;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseConfig {
	
	protected final Map<String, Object> keyMap;
	
	public BaseConfig() {
		this.keyMap = new HashMap<>();
	}
	
	/**
	 * Replaces a value in the config if the key exists
	 * @param key The key to replace the value of
	 * @param value The value to replace the old value with
	 */
	public void replaceValue(String key, Object value) {
		this.keyMap.replace(key, value);
	}
	
	/**
	 * Puts a key in the configuration
	 * @param key The key to put into the config
	 * @param value The value to set the key to.
	 */
	public void setValue(String key, Object value) {
		this.keyMap.put(key, value);
	}
	
	public Object getValue(String key) {
		return keyMap.get(key);
	}

}
