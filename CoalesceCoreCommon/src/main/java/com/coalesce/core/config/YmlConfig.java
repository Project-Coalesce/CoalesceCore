package com.coalesce.core.config;

import com.coalesce.core.plugin.ICoPlugin;
import org.yaml.snakeyaml.Yaml;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

public class YmlConfig extends BaseConfig {
	
	private final ICoPlugin plugin;
	private final File path, file;
	private final String name;
	private final Yaml yaml;
	
	public YmlConfig(String name, ICoPlugin plugin) {
		this.plugin = plugin;
		this.name = name;
		
		if (!name.contains(File.separator)) {
			this.path = plugin.getPluginFolder();
			this.file = new File(path.getAbsolutePath() + File.separator + name + ".yml");
		} else {
			int last = name.lastIndexOf(File.separator);
			String fileName = name.substring(last + 1);
			String directory = name.substring(0, last);
			this.path = new File(plugin.getPluginFolder().getAbsolutePath() + File.separator + directory);
			this.file = new File(this.path + File.separator + fileName + ".yml");
		}
		
		if (!path.exists()) {
			path.mkdirs();
			try {
				file.createNewFile();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (!file.exists()) {
			try {
				file.createNewFile();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		Object ymlObject = null;
		this.yaml = new Yaml();
		try {
			ymlObject = yaml.load(new FileInputStream(this.file));
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		if (ymlObject != null) {
			convertFile((Map<?, ?>)ymlObject, null);
		}
		
		System.out.println("BREAK ====================================================================================");
		this.keyMap.forEach((k,v) -> {
			System.out.println("KEY: " + k);
			System.out.println("VALUE: " + v);
		});
		
	}
	
	private void convertFile(Map<?, ?> input, String key) {
		input.forEach((k,v) -> {
			if (v instanceof Map) {
				if (key != null) {
					convertFile((Map)v, key + "." + k);
				}
				else convertFile((Map)v, (String)k);
			} else keyMap.put(key + "." + k, v);
		});
	}
	
	private void write(Object data) {
		yaml.dumpAsMap(data);
	}
	
	private void createObjects(Map<String, Object> input) {
		input.forEach((k,v) -> {
		
		});
	}
	
}
