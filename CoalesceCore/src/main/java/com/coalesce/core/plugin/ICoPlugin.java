package com.coalesce.core.plugin;

import com.coalesce.core.Color;
import com.coalesce.core.chat.CoFormatter;
import com.coalesce.core.command.base.CommandStore;
import com.coalesce.core.config.base.IConfig;
import com.coalesce.core.i18n.CoLang;
import com.coalesce.core.i18n.LocaleStore;
import com.coalesce.core.i18n.Translatable;
import com.coalesce.core.session.SessionStore;
import org.bukkit.craftbukkit.libs.jline.console.ConsoleReader;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@SuppressWarnings( {"unused", "unchecked"} )
public interface ICoPlugin<M extends Enum & Translatable> extends Plugin {
    
    /**
     * Gets a map of all the current CoPlugins loaded on the server
     *
     * @return The map of CoPlugins
     */
    Map<String, ICoPlugin> getCoPlugins();
    
    /**
     * Gets a CoPlugin by name
     *
     * @param name The name of the plugin to get.
     * @return The plugin if running
     */
    default ICoPlugin getCoPlugin(String name) {
        return getCoPlugins().get(name);
    }
    
    /**
     * Checks if an update exists for this plugin.
     *
     * @param repositoryOwner The user or organization that this repository is held in.
     * @param repositoryName  The name of the repository.
     * @param autoUpdate      Whether or not to download a new version if it's out.
     */
    void updateCheck(String repositoryOwner, String repositoryName, boolean autoUpdate);
    
    /**
     * Gets the plugin version
     *
     * @return The version of the plugin.
     */
    String getVersion();
    
    /**
     * Method called when the plugin is enabled
     */
    void onPluginEnable() throws Exception;
    
    /**
     * Method called when the plugin is disabled
     */
    void onPluginDisable() throws Exception;
    
    /**
     * Method called when the plugin is loaded
     */
    void onPluginLoad() throws Exception;
    
    /**
     * Get the session store of the plugin
     *
     * @return The plugin session store.
     */
    SessionStore getSessionStore();
    
    /**
     * Get a set of modules the plugin has.
     *
     * @return This plugin's modules.
     */
    List<ICoModule> getModules();
    
    /**
     * Get the session store of another plugin
     *
     * @param plugin The plugin to get the session store from
     * @return The plugins session store
     */
    SessionStore getSessionStore(ICoPlugin plugin);
    
    /**
     * Adds modules to this plugin
     *
     * @param modules The module(s) to add
     */
    default void addModules(ICoModule... modules) {
        Collections.addAll(getModules(), modules);
    }
    
    /**
     * Gets a module by class.
     *
     * @param clazz The class that extends CoModule
     * @param <T>   A module class
     * @return The module.
     */
    default <T extends ICoModule> T getModule(Class<T> clazz) {
        return getModules().stream().filter(module -> module.getClass().equals(clazz)).map(module -> ((T)module)).iterator().next();
    }
    
    /**
     * Enables all this plugins modules.
     */
    default void enableModules() {
        getModules().forEach(ICoModule::enable);
    }
    
    /**
     * Disables all this plugins modules.
     */
    default void disableModules() {
        getModules().forEach(ICoModule::disable);
    }
    
    /**
     * Registers a listener class
     *
     * @param listener The listener to register
     */
    void registerListener(Object listener);
    
    /**
     * Unregisters a listener class
     *
     * @param listener The listener to unregister
     */
    void unregisterListener(Object listener);
    
    /**
     * Registers an array of listeners
     *
     * @param listeners The listeners to register
     */
    default void registerListeners(Object... listeners) {
        Arrays.asList(listeners).forEach(this::registerListeners);
    }
    
    /**
     * Sets the displayname of the plugin
     *
     * @param displayName The plugin displayname.
     */
    void setDisplayName(String displayName);
    
    /**
     * Gets the display name of the plugin.
     *
     * @return The plugin with the correct plugin color.
     */
    String getDisplayName();
    
    /**
     * Gets the unedited name of the plugin. (Will not be affected by {@link #setDisplayName(String)})
     *
     * @return The name of the plugin
     */
    String getRealName();
    
    /**
     * Sets the color of this plugin
     *
     * @param color The plugin color.
     */
    void setPluginColor(Color color);
    
    /**
     * Gets the color of the plugin.
     *
     * @return Defaults to white if color has not been specified.
     */
    Color getPluginColor();
    
    /**
     * Gets the plugin logger
     *
     * @return The plugin logger.
     */
    CoLogger getCoLogger();
    
    /**
     * Gets the plugin formatter
     *
     * @return Plugin formatter.
     */
    CoFormatter getCoFormatter();
    
    /**
     * Gets the command store
     *
     * @return The command store.
     */
    CommandStore<M> getCommandStore();
    
    /**
     * Gets the current plugins data folder.
     *
     * @return plugins data folder. Eg. plugins/CoalesceCoreBukkit
     */
    File getPluginFolder();
    
    /**
     * Gets the folder where all the plugins are held.
     * @return The plugins directory.
     */
    default File getPluginsDirectory() {
        return getPluginFolder().getParentFile();
    }
    
    /**
     * Gets the plugin jar file
     *
     * @return The plugin jar file.
     */
    File getPluginJar();
    
    LocaleStore<M> getLocaleStore();
    
    default void setLocale(Locale locale) {
        getLocaleStore().setDefaultLocale(locale);
    }
    
    default void setLocaleClassType(Class<M> cls) {
        getLocaleStore().setClassType(cls);
    }
    
    default void loadCoLang(CoLang<M> coLang) {
        getLocaleStore().loadCoLang(coLang);
    }
    
    default void loadCoLang(IConfig file, Locale locale) {
        getLocaleStore().loadCoLang(new CoLang<>(file, getLocaleStore(), locale));
    }
    
    default void loadCoLang(File file, Locale locale) {
        getLocaleStore().loadCoLang(file, locale);
    }
    
    default void loadCoLang(String file, Locale locale) {
        getLocaleStore().loadCoLang(file, locale);
    }
    
    default CoLang<M> getCoLang(Locale locale) {
        return getLocaleStore().getCoLang(locale);
    }
    
    /**
     * Moves a file or directory from a
     * @param pathInJar The path to the file in the jar file. MUST HAVE EXTENSION
     * @param destination where you want this file moved to. MUST HAVE EXTENSION
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    default void installFile(String pathInJar, String destination) {
        if (!destination.contains(".")) throw new RuntimeException("File type must be specified.");
        try {
            File path;
            File file;
    
            if (!destination.contains(File.separator)) {
                path = getPluginFolder();
                file = new File(path.getAbsolutePath() + File.separator + destination);
            }
            else {
                int last = destination.lastIndexOf(File.separator);
                String fileName = destination.substring(last + 1);
                String directory = destination.substring(0, last);
                path = new File(getPluginFolder().getAbsolutePath() + File.separator + directory);
                file = new File(path + File.separator + fileName);
            }
            
            if (!path.exists()) path.mkdirs();
            if (!file.exists()) file.createNewFile();
            else {
                file.delete();
                file.createNewFile();
            }
            InputStream stream;
            OutputStream resStreamOut;
    
            stream = this.getClass().getResourceAsStream(pathInJar);
            
            if (stream == null) {
                throw new RuntimeException("the path specified could not be found in the jar file.");
            }
    
            int readBytes;
            byte[] buffer = new byte[4096];
            resStreamOut = new FileOutputStream(file);
            while ((readBytes = stream.read(buffer)) > 0) {
                resStreamOut.write(buffer, 0, readBytes);
            }
            stream.close();
            resStreamOut.close();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    
    }
    
    ConsoleReader getConsoleReader();
    
}
