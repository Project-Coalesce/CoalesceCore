package com.coalesce.core.bukkit;

import com.coalesce.core.Color;
import com.coalesce.core.Platform;
import com.coalesce.core.chat.CoFormatter;
import com.coalesce.core.command.base.CommandStore;
import com.coalesce.core.config.ConfigManager;
import com.coalesce.core.plugin.CoLogger;
import com.coalesce.core.plugin.ICoModule;
import com.coalesce.core.plugin.ICoPlugin;
import com.coalesce.core.session.SessionStore;
import com.coalesce.core.update.InstallUpdateThread;
import com.coalesce.core.update.UpdateCheck;
import jline.console.ConsoleReader;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class CoPlugin extends JavaPlugin implements ICoPlugin, Listener {
	
	private final SessionStore sessionStore = new SessionStore();
	private final List<ICoModule> modules = new LinkedList<>();
	private CommandStoreBukkit commandStore;
	private Color pluginColor = Color.WHITE;
	private boolean updateNeeded = false;
	private ConfigManager configManager;
	private CoFormatter formatter;
	private String displayName;
	private CoLogger logger;
	private File updateFile;
	
	@Override
	public final void onEnable() {
		displayName = getName();
		logger = new CoLogger(this);
		formatter = new CoFormatter(this);
		configManager = new ConfigManager(this);
		commandStore = new CommandStoreBukkit(this);
		CoreBukkit.addCoPlugin(getRealName(), this);
		CoreBukkit.addSessionStore(this, sessionStore);
		
		try {
			this.onPluginEnable();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		commandStore.registerObjects();
		enableModules();
	}
	
	@Override
	public final void onDisable() {
		try {
			this.onPluginDisable();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		disableModules();
		
		if (updateNeeded) {
			new InstallUpdateThread(updateFile, getFile()).start();
		}
	}
	
	@Override
	public final void onLoad() {
		try {
			this.onPluginLoad();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public abstract void onPluginEnable() throws Exception;
	
	@Override
	public abstract void onPluginDisable() throws Exception;
	
	@Override
	public void onPluginLoad() throws Exception {
	
	}
	
	//
	//
	
	@Override
	public String getDisplayName() {
		return displayName;
	}
	
	//
	//
	
	@Override
	public String getRealName() {
		return getName();
	}
	
	//
	//
	
	@Override
	public void setDisplayName(String displayName) {
		this.displayName = pluginColor + displayName + Color.RESET;
	}
	
	//
	//
	
	
	@Override
	public Color getPluginColor() {
		return pluginColor;
	}
	
	//
	//
	
	@Override
	public void setPluginColor(Color pluginColor) {
		this.pluginColor = pluginColor;
		this.displayName = pluginColor + Color.stripColor(displayName) + Color.RESET;
	}
	
	//
	//
	
	@Override
	public final SessionStore getSessionStore() {
		return sessionStore;
	}
	
	//
	//
	
	@Override
	public final List<ICoModule> getModules() {
		return modules;
	}
	
	//
	//
	
	@Override
	public final SessionStore getSessionStore(ICoPlugin plugin) {
		return CoreBukkit.getSessionStores().get(plugin);
	}
	
	//
	//
	
	@Override
	public final void registerListener(Object listener) {
		Bukkit.getPluginManager().registerEvents((Listener)listener, this);
	}
	
	//
	//
	
	@Override
	public final void unregisterListener(Object listener) {
		HandlerList.unregisterAll((Listener)listener);
	}
	
	//
	//
	
	@Override
	public CoLogger getCoLogger() {
		return logger;
	}
	
	//
	//
	
	@Override
	public CoFormatter getCoFormatter() {
		return formatter;
	}
	
	//
	//
	
	@Override
	public Platform getPlatform() {
		return Platform.BUKKIT;
	}
	
	//
	//
	
	@Override
	public CommandStore getCommandStore() {
		return commandStore;
	}
	
	//
	//
	
	@Override
	public File getPluginFolder() {
		return getDataFolder();
	}
	
	//
	//
	
	
	@Override
	public ConfigManager getConfigManager() {
		return configManager;
	}
	
	//
	//
	
	@Override
	public Map<String, ICoPlugin> getCoPlugins() {
		return CoreBukkit.getPlugins();
	}
	
	//
	//
	
	@Override
	public String getVersion() {
		return getDescription().getVersion();
	}
	
	//
	//
	
	
	@Override
	public void updateCheck(String repositoryOwner, String repositoryName, boolean autoUpdate) {
		Bukkit.getScheduler().runTaskAsynchronously(this, new UpdateCheck(this, new UpdateLogger(this), repositoryOwner, repositoryName, autoUpdate));
	}
	
	//
	//
	
	@Override
	public void setUpdateFile(File file) {
		this.updateFile = file;
	}
	
	//
	//
	
	@Override
	public void setUpdateNeeded(boolean value) {
		this.updateNeeded = value;
	}
	
	//
	//
	
	@Override
	public ConsoleReader getConsoleReader() {
		return CoreBukkit.getReader();
	}
	
	//
	//
	
	
	@Override
	public File getPluginJar() {
		return getFile();
	}
	
	//
	//
	
	@Override
	public Set<String> getOnlinePlayers() {
		Set<String> names = new HashSet<>();
		Bukkit.getOnlinePlayers().stream().map(Player::getName).forEach(names::add);
		return names;
	}
}
