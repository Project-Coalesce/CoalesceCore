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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class YmlConfig extends BaseConfig {
	
	private final Collection<IEntry> entries;
	private final Representer representer;
	private final DumperOptions options;
	private final Map<String, Object> mappedFile;
	private final File path, file;
	private final Yaml yaml;
	
	public YmlConfig(String name, ICoPlugin plugin) {
		super(name, plugin);
		this.entries = new HashSet<>();
		this.mappedFile = new HashMap<>();
		this.representer = new Representer();
		this.options = new DumperOptions();
		
		options.setIndent(2);
		options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
		representer.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
		
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
	
	//read the yml object from the file
	//Once read, save the object in a map.
	//Format:
	/*
	test: //Map
	  test2:  //Map
	    butt: hi //String
	    bum: hello //String at this point::: Map<String, Map<String, Map<String, Object>>>
	  test3: //Map
	    foot: bye //String
	    feet: mop //String
	    bork: //Map
	      yes: no //String
	      no: yes //String
    testB: //Map
      testB2: //Map
        butt: hi //String
	    bum: hello //String
	  testB3: //Map
	    foot: bye //String
	    feet: mop //String
	 */
	//Map<String, Object>
	//First string would be a base value everything inherits
	//Iterate through the first bottom level map values. if the value is a map then loop through that and so on until the value isnt a map
	
	private void collectKeys(Map<String, Object> input, String key) {
		input.forEach((k,v) -> {
			if (v instanceof Map) {
				if (key != null) collectKeys((Map)v, key + "." + k);
				else collectKeys((Map)v, k);
			} else keyMap.put(key + "." + k, v);
		});
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
	
	private void setVal(String key, Object value, Map<String, Object> map, boolean isSet) {
		String current = key.substring(key.contains(".") ? key.lastIndexOf(".") : 0);
		Map<String, Object> temp = new HashMap<>();
		if (!isSet) {
			if (key.contains(".")) {
				temp.put(current, value);
				setVal(key.substring(0, key.lastIndexOf(".")), value, temp, true);
			} else mappedFile.put(current, value);
		} else {
			if (key.contains(".")) {
				temp.put(current, map);
				setVal(key.substring(0, key.lastIndexOf(".")), value, temp, true);
			} else mappedFile.put(current, map);
		}
	}
	
	private Object get(String key, Map<String, Object> map) {
		String current = key.substring(0, key.indexOf("."));
		Object object = map.get(current);
		if (object instanceof Map) {
			return get(key.substring(key.indexOf(".")), (Map<String, Object>) object);
		} else return object;
	}
	
	private void setValue(String path, Object val) {
		if (val == null) {
			keyMap.remove(path);
		}
		else keyMap.put(path, val);
		setVal(path, val, null, false);
		try {
			save();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void save() throws IOException {
		if (!getDirectory().exists()) {
			getDirectory().mkdirs();
		}
		if (!getFile().exists()) {
			getFile().createNewFile();
		}
	
		Writer fileWriter = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
		System.out.println(mappedFile.toString());
		yaml.dump(mappedFile, fileWriter);
	}
	
}
