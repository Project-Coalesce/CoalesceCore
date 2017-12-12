package com.coalesce.core.config;

import com.coalesce.core.config.base.BaseConfig;
import com.coalesce.core.config.common.Entry;
import com.coalesce.core.plugin.ICoPlugin;
import org.simpleyaml.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Set;

public class YmlConfig extends BaseConfig {
	
	private final YamlConfiguration config;
	private final File file;
	
	public YmlConfig(String name, ICoPlugin plugin) {
		this(name, plugin, true);
	}
	
	@SuppressWarnings("ResultOfMethodCallIgnored")
	public YmlConfig(String name, ICoPlugin plugin, boolean createNow) {
		super(name, plugin, createNow);
		
		File path;
		if (!name.contains(File.separator)) {
			path = plugin.getPluginFolder();
			this.file = new File(path.getAbsolutePath() + File.separator + name + ".yml");
		} else {
			int last = name.lastIndexOf(File.separator);
			String fileName = name.substring(last + 1);
			String directory = name.substring(0, last);
			path = new File(plugin.getPluginFolder().getAbsolutePath() + File.separator + directory);
			this.file = new File(path + File.separator + fileName + ".yml");
		}
		
		if (createNow) {
			if (!path.exists()) {
				path.mkdirs();
				create();
			}
			if (!file.exists()) {
				create();
			}
		}
		
		this.config = YamlConfiguration.loadConfiguration(file);
		
		config.getKeys(true).forEach(key -> {
			if (getEntry(key) == null) {
				entries.add(new Entry(this, key, config.get(key)));
			}
		});
	}
	
	@Override
	public Set<String> getKeys(boolean deep) {
		return config.getKeys(deep);
	}
	
	@Override
	public void addEntry(String path, Object value) {
		if (config.get(path) == null) {
			entries.add(new Entry(this, path, value));
			setValue(path, value);
		}
	}
	
	@Override
	public void setEntry(String path, Object value) {
		setValue(path, value);
		if (value == null || contains(path, true)) entries.remove(getEntry(path));
		if (value != null) {
			entries.add(new Entry(this, path, value));
		}
	}
	
	@Override
	public File getFile() {
		return file;
	}
	
	private void setValue(String path, Object val) {
		config.set(path, val);
		try {
			config.save(file);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
