package com.coalesce.core.sponge;

import com.coalesce.core.Color;
import com.coalesce.core.Platform;
import com.coalesce.core.chat.CoFormatter;
import com.coalesce.core.command.base.CommandStore;
import com.coalesce.core.plugin.CoLogger;
import com.coalesce.core.plugin.ICoModule;
import com.coalesce.core.plugin.ICoPlugin;
import com.coalesce.core.session.SessionStore;
import com.coalesce.core.update.InstallUpdateThread;
import com.coalesce.core.update.UpdateCheck;
import jline.console.ConsoleReader;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartingServerEvent;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.scheduler.Task;

import java.io.File;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class CoPlugin implements ICoPlugin {
	
	private final SessionStore sessionStore = new SessionStore();
	private final List<ICoModule> modules = new LinkedList<>();
	private CommandStoreSponge commandStore;
	private Color pluginColor = Color.WHITE;
	private PluginContainer pluginContainer;
	private boolean updateNeeded = false;
	private CoFormatter formatter;
	private String displayName;
	private CoLogger logger;
	private File updateFile;
	
	@Listener
	@SuppressWarnings("unused")
	public final void onEnable(GameStartingServerEvent e) {
		pluginContainer = Sponge.getPluginManager().fromInstance(this).get();
		displayName = pluginContainer.getName();
		logger = new CoLogger(this);
		formatter = new CoFormatter(this);
		commandStore = new CommandStoreSponge(this);
		CoreSponge.addCoPlugin(getRealName(), this);
		CoreSponge.addSessionStore(this, sessionStore);
		try {
			onPluginEnable();
		}
		catch (Exception e1) {
			e1.printStackTrace();
		}
		commandStore.registerObjects();
	}
	
	@Listener
	@SuppressWarnings("unused")
	public final void onDisable(GameStoppingServerEvent e) {
		try {
			onPluginDisable();
		}
		catch (Exception e1) {
			e1.printStackTrace();
		}
		if (updateNeeded) {
			new InstallUpdateThread(updateFile, getPluginJar());
		}
	}
	@Listener
	@SuppressWarnings("unused")
	public final void onLoad(GameInitializationEvent e) {
		try {
			onPluginLoad();
		}
		catch (Exception e1) {
			e1.printStackTrace();
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
		return pluginContainer.getName();
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
		this.displayName = pluginColor + displayName + Color.RESET;
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
		return CoreSponge.getSessionStores().get(plugin);
	}
	
	//
	//
	
	@Override
	public final void registerListener(Object listener) {
		Sponge.getEventManager().registerListeners(this, listener);
	}
	
	//
	//
	
	@Override
	public final void unregisterListener(Object listener) {
		Sponge.getEventManager().unregisterListeners(listener);
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
		return Platform.SPONGE;
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
		return Sponge.getConfigManager().getPluginConfig(this).getDirectory().toFile();
	}
	
	//
	//
	
	@Override
	public Map<String, ICoPlugin> getCoPlugins() {
		return CoreSponge.getPlugins();
	}
	
	//
	//
	
	@Override
	public void updateCheck(String repositoryOwner, String repositoryName, boolean autoUpdate) {
		Task.builder().async().execute(new UpdateCheck(this, new UpdateLogger(this), repositoryOwner, repositoryName, autoUpdate)).submit(this);
	}
	
	//
	//
	
	@Override
	public String getVersion() {
		return pluginContainer.getVersion().get();
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
		return CoreSponge.getReader();
	}
	
	//
	//
	
	@Override
	public File getPluginJar() {
		return pluginContainer.getSource().get().toFile();
	}
	
	//
	//
	
	@Override
	public Set<String> getOnlinePlayers() {
		Set<String> names = new HashSet<>();
		Sponge.getServer().getOnlinePlayers().stream().map(Player::getName).forEach(names::add);
		return names;
	}
}
