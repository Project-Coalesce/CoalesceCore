package com.coalesce.core.bukkit;

import com.coalesce.core.Color;
import com.coalesce.core.command.BuilderTest;
import com.coalesce.core.command.TestCommand;
import com.coalesce.core.config.YmlConfig;
import com.coalesce.core.plugin.ICoPlugin;
import com.coalesce.core.session.SessionStore;
import com.coalesce.core.update.InstallerStartup;
import jline.console.ConsoleReader;
import jline.console.completer.CandidateListCompletionHandler;

import java.util.HashMap;
import java.util.Map;

public class CoreBukkit extends CoPlugin {

    //Creates a map of all the session stores for each plugin
    private static final Map<ICoPlugin, SessionStore> SESSION_STORES = new HashMap<>();
    private static final Map<String, ICoPlugin> PLUGINS = new HashMap<>();
    private static ConsoleReader consoleReader;
    private static InstallerStartup installer;

    @Override
    public void onPluginEnable() throws Exception {
        updateCheck("google", "android-classyshark", true);
        setDisplayName("CoalesceCore");
        setPluginColor(Color.YELLOW);
        getCommandStore().registerCommand(new TestCommand());
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

}

