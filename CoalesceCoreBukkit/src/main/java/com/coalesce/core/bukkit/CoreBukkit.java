package com.coalesce.core.bukkit;

import com.coalesce.core.Coalesce;
import com.coalesce.core.Color;
import com.coalesce.core.command.BuilderTest;
import com.coalesce.core.command.TestCommand;
import com.coalesce.core.config.YmlConfig;
import com.coalesce.core.plugin.ICoPlugin;
import com.coalesce.core.session.SessionStore;
import com.coalesce.core.update.InstallerStartup;
import com.coalesce.core.wrappers.CoPlayer;
import jline.console.ConsoleReader;
import jline.console.completer.CandidateListCompletionHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CoreBukkit extends CoPlugin {

    //Creates a map of all the session stores for each plugin
    private static final Map<ICoPlugin, SessionStore> SESSION_STORES = new HashMap<>();
    private static final Map<String, ICoPlugin> PLUGINS = new HashMap<>();
    private static final Map<String, CoPlayer<Player>> COPLAYERS = new HashMap<>();
    private static ConsoleReader consoleReader;
    private static InstallerStartup installer;

    @Override
    public void onPluginEnable() throws Exception {
        Coalesce.setCoalesce(this);
        updateCheck("Project-Coalesce", "CoalesceCore", true);
        setDisplayName("CoalesceCore");
        setPluginColor(Color.YELLOW);
        getCommandStore().registerCommand(new TestCommand());
        registerListener(this);
        new BuilderTest(this);
        new YmlConfig("groups", this);
    }

    @Override
    public void onPluginLoad() throws Exception {
        installer = new InstallerStartup(this);
        consoleReader = new ConsoleReader(System.in, System.out);
        consoleReader.setCompletionHandler(new CandidateListCompletionHandler());
        consoleReader.setExpandEvents(false);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        COPLAYERS.put(e.getPlayer().getName(), new BukkitPlayer(e.getPlayer(), this));
    }
    
    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        COPLAYERS.remove(e.getPlayer().getName());
    }
    
    @Override
    public void onPluginDisable() throws Exception {
        installer.start();
    }

    static void addSessionStore(ICoPlugin plugin, SessionStore sessionStore) {
        SESSION_STORES.put(plugin, sessionStore);
    }

    static Map<ICoPlugin, SessionStore> getSessionStores() {
        return SESSION_STORES;
    }

    static Map<String, ICoPlugin> getPlugins() {
        return PLUGINS;
    }

    static void addCoPlugin(String name, ICoPlugin plugin) {
        PLUGINS.put(name, plugin);
    }

    static InstallerStartup getInstaller() {
        return installer;
    }
    
    static ConsoleReader getReader() {
        return consoleReader;
    }
    
    static Map<String, CoPlayer<Player>> getPlayersMap() {
        return COPLAYERS;
    }
    
    static Collection<CoPlayer<Player>> getPlayers() {
        return COPLAYERS.values();
    }
    
    static CoPlayer<Player> getPlayer(String name) {
        return COPLAYERS.get(name);
    }
    
}

