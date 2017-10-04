package com.coalesce.core.config;

import com.coalesce.core.config.base.BaseConfig;
import com.coalesce.core.config.base.IConfig;
import com.coalesce.core.config.base.IEntry;
import com.coalesce.core.config.base.ISection;
import com.coalesce.core.plugin.ICoPlugin;

import java.util.Set;

public final class Section implements ISection {
	
	private final BaseConfig config;
	private final String path;
	private Object value;
	
	public Section(String path, Object value, BaseConfig config) {
		this.path = path;
		this.value = value;
		this.config = config;
	}
	
	@Override
	public Set<String> getKeys(boolean deep) {
		return null;
	}
	
	@Override
	public Set<IEntry> getEntries() {
		return null;
	}
	
	@Override
	public ISection getSection(String path) {
		return null;
	}
	
	@Override
	public boolean contains(String path) {
		return false;
	}
	
	@Override
	public String getCurrentPath() {
		return null;
	}
	
	@Override
	public String getName() {
		return null;
	}
	
	@Override
	public IConfig getConfig() {
		return null;
	}
	
	@Override
	public ICoPlugin getPlugin() {
		return null;
	}
	
	@Override
	public IEntry getEntry(String path) {
		return null;
	}
	
	@Override
	public ISection getParent() {
		return null;
	}
}
