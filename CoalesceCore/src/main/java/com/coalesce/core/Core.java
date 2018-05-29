package com.coalesce.core;

import com.coalesce.core.command.BuilderTest;
import com.coalesce.core.plugin.ICoPlugin;
import com.coalesce.core.session.SessionStore;
import com.coalesce.core.update.InstallerStartup;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.libs.jline.console.ConsoleReader;

import java.util.HashMap;
import java.util.Map;

public class Core extends CoPlugin {
    
    private static final Map<ICoPlugin, SessionStore> SESSION_STORES = new HashMap<>();
    private static final Map<String, ICoPlugin> PLUGINS = new HashMap<>();
    private static InstallerStartup installer;
    private static ConsoleReader consoleReader;
    private static CoreConfig config;
    
    @Override
    public void onPluginEnable() throws Exception {
        Coalesce.setCoalesce(this);
        updateCheck("Project-Coalesce", "CoalesceCore", true);
        setDisplayName("CoalesceCore");
        setPluginColor(Color.YELLOW);
        registerListener(this);
        config = new CoreConfig(this);
        new BuilderTest(this);
    }
    
    @Override
    public void onPluginLoad() throws Exception {
        installer = new InstallerStartup(this);
        consoleReader = (ConsoleReader)Bukkit.getServer().getClass().getDeclaredMethod("getReader", null).invoke(Bukkit.getServer(), null);
    }
    
    @Override
    public void onPluginDisable() throws Exception {
        installer.start();
    }
    
    static CoreConfig getCoreConfig() {
        return config;
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
    
}
