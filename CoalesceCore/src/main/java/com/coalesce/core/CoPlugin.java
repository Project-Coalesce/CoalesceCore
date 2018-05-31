package com.coalesce.core;

import com.coalesce.core.chat.CoFormatter;
import com.coalesce.core.command.base.CommandStore;
import com.coalesce.core.i18n.LocaleStore;
import com.coalesce.core.i18n.Translatable;
import com.coalesce.core.plugin.CoLogger;
import com.coalesce.core.plugin.ICoModule;
import com.coalesce.core.plugin.ICoPlugin;
import com.coalesce.core.session.SessionStore;
import com.coalesce.core.update.UpdateLookup;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.libs.jline.console.ConsoleReader;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public abstract class CoPlugin<T extends Enum & Translatable> extends JavaPlugin implements ICoPlugin<T>, Listener{
    
    private final SessionStore sessionStore = new SessionStore();
    private final List<ICoModule> modules = new LinkedList<>();
    private CommandStore<T> commandStore;
    private LocaleStore<T> localeStore;
    private Color pluginColor = Color.WHITE;
    private CoFormatter formatter;
    private String displayName;
    private CoLogger logger;
    
    @Override
    public final void onEnable() {
        displayName = getName();
        logger = new CoLogger(this);
        formatter = new CoFormatter(this);
        commandStore = new CommandStore<>(this);
        localeStore = new LocaleStore<>(this);
        Core.addCoPlugin(getRealName(), this);
        Core.addSessionStore(this, sessionStore);
        
        try {
            this.onPluginEnable();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
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
        return Core.getSessionStores().get(plugin);
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
    public CommandStore<T> getCommandStore() {
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
    public Map<String, ICoPlugin> getCoPlugins() {
        return Core.getPlugins();
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
    public final void updateCheck(String repositoryOwner, String repositoryName, boolean autoUpdate) {
        Bukkit.getScheduler().runTaskAsynchronously(this, new UpdateLookup(this, Core.getInstaller(), repositoryOwner, repositoryName, getVersion(), autoUpdate));
    }
    
    //
    //

    @Override
    public ConsoleReader getConsoleReader() {
        return Core.getReader();
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
    public LocaleStore<T> getLocaleStore() {
        return localeStore;
    }
}
