package com.coalesce.core.config;

import com.coalesce.core.config.base.BaseConfig;
import com.coalesce.core.config.base.IEntry;
import com.coalesce.core.config.common.Entry;
import com.coalesce.core.plugin.ICoPlugin;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.representer.Representer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class YmlConfig extends BaseConfig {
	
	private final Collection<IEntry> entries;
	private final Map<String, Object> mappedFile;
	private final File file;
	private final Yaml yaml;
	
	@SuppressWarnings({"ResultOfMethodCallIgnored", "unchecked"})
	public YmlConfig(String name, ICoPlugin plugin) {
		super(name, plugin);
		this.entries = new HashSet<>();
		this.mappedFile = new HashMap<>();
		Representer representer = new Representer();
		DumperOptions options = new DumperOptions();
		
		options.setIndent(2);
		options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
		representer.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
		
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
		
		if (! path.exists()) {
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
		
		this.yaml = new Yaml(representer, options);
		BufferedReader reader;
		StringBuilder builder = new StringBuilder();
		
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(this.file), StandardCharsets.UTF_8));
			String line;
			
			while ((line = reader.readLine()) !=null) {
				builder.append(line);
				builder.append('\n');
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if (yaml.load(builder.toString()) == null) {
			mappedFile.putAll(new HashMap<>());
		} else mappedFile.putAll((Map<String, Object>)yaml.load(builder.toString()));
		collectKeys(mappedFile, null);
		
		keyMap.forEach((k, v) -> {
			if (getEntry(k) == null) {
				entries.add(new Entry(this, k, v));
			}
		});
	}
	
	@Override
	public Set<String> getKeys(boolean deep) {
		if (deep) return keyMap.keySet();
		else return mappedFile.keySet();
	}
	
	@Override
	public Collection<IEntry> getEntries() {
		return entries;
	}
	
	@Override
	public void addEntry(String path, Object value) {
		IEntry entry;
		if (!keyMap.containsKey(path)) {
			entry = new Entry(this, path, value);
			setValue(entry.getPath(), entry.getValue());
		}
		if (getEntry(path) == null) entries.add(new Entry(this, path, keyMap.get(path)));
	}
	
	@Override
	public void setEntry(String path, Object value) {
		if (value == null) {
			entries.remove(getEntry(path));
			setValue(path, null);
			return;
		}
		IEntry entry = new Entry(this, path, value);
		if (getEntry(path) == null) {
			setValue(path, value);
			entries.add(entry);
		}
		else {
			entries.remove(getEntry(path));
			setValue(path, value);
			entries.add(entry);
		}
		
	}
	
	@Override
	public File getFile() {
		return file;
	}
	
	@SuppressWarnings("unchecked")
	private void collectKeys(Map<String, Object> input, String key) {
		input.forEach((k,v) -> {
			if (v instanceof Map) {
				if (key != null) collectKeys((Map<String, Object>)v, key + "." + k);
				else collectKeys((Map<String, Object>)v, k);
			} else keyMap.put(key + "." + k, v);
		});
	}
	
	@SuppressWarnings("unchecked")
	private void composeMap(String key, Object value, Map<String, Object> map, boolean isSet) {
		String current = key.substring(key.contains(".") ? key.lastIndexOf(".") + 1 : 0);
		Map<String, Object> temp;
		Object previousMap = get(key.substring(0, key.contains(".") ? key.lastIndexOf(".") : key.length()), mappedFile);
		
		if (previousMap instanceof Map && !((Map<String, Object>)previousMap).isEmpty()) {
			temp = (Map<String, Object>)previousMap;
		} else temp = new HashMap<>();
		
		if (!isSet) {
			if (key.contains(".")) {
				temp.put(current, value);
				composeMap(key.substring(0, key.lastIndexOf(".")), value, temp, true);
			} else mappedFile.put(current, value);
		} else {
			if (key.contains(".")) {
				temp.put(current, map);
				composeMap(key.substring(0, key.lastIndexOf(".")), value, temp, true);
			} else mappedFile.put(current, map);
		}
	}
	
	@SuppressWarnings("unchecked")
	private Object get(String key, Map<String, Object> map) {
		String current = key.substring(0, key.contains(".") ? key.indexOf(".") : key.length());
		if (map.get(current) == null) {
			return null;
		}
		if (key.contains(".")) return get(key.substring(key.indexOf(".") + 1), (Map<String, Object>)map.get(current));
		else return map.get(current);
	}
	
	private void setValue(String path, Object val) {
		if (val == null) {
			keyMap.remove(path);
		}
		else keyMap.put(path, val);
		composeMap(path, val, null, false);
		try {
			save();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("ResultOfMethodCallIgnored")
	private void save() throws IOException {
		if (!getDirectory().exists()) {
			getDirectory().mkdirs();
		}
		if (!getFile().exists()) {
			getFile().createNewFile();
		}
		Writer fileWriter = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
		yaml.dump(mappedFile, fileWriter);
	}
	
}
