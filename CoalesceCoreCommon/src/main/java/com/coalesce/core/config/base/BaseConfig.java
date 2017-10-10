package com.coalesce.core.config.base;

import com.coalesce.core.plugin.ICoPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class BaseConfig implements IConfig {
	
	private final String name;
	private final ICoPlugin plugin;
	protected final Map<String, Object> keyMap;
	
	public BaseConfig(String name, ICoPlugin plugin) {
		this.keyMap = new HashMap<>();
		this.plugin = plugin;
		this.name = name;
	}
	
	@Override
	public ICoPlugin getPlugin() {
		return plugin;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public Object getValue(String path) {
		return keyMap.get(path);
	}
}
