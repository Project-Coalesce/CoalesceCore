package com.coalesce.core.config;

public final class Section {
	
	private final BaseConfig config;
	private final String key;
	private Object value;
	
	public Section(String key, Object value, BaseConfig config) {
		this.key = key;
		this.value = value;
		this.config = config;
	}
	
	public String getKey() {
		return key;
	}
	
	public Object getValue() {
		return value;
	}
	
	public void setValue(Object value) {
		config.replaceValue(key, value);
		this.value = value;
	}
}
