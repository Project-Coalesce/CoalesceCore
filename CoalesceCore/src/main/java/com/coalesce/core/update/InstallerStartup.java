package com.coalesce.core.update;

import com.coalesce.core.plugin.ICoPlugin;
import javafx.util.Pair;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredListener;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class InstallerStartup {
    
    private final ICoPlugin plugin;
    private final Map<ICoPlugin, Pair<String, String>> files;
    private final File pluginFolder, updateFolder;
    
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public InstallerStartup(ICoPlugin plugin) {
        this.plugin = plugin;
        this.files = new HashMap<>();
        this.pluginFolder = plugin.getPluginsDirectory();
        this.updateFolder = new File(pluginFolder + File.separator + "Updates");
        if (!updateFolder.exists()) updateFolder.mkdirs();
    }
    
    @SuppressWarnings("all")
    public void addUpdate(ICoPlugin plugin, String oldFileName, String newFileName) {
        plugin.getCoLogger().info(oldFileName + " will be updated to " + newFileName + " upon restart.");
        files.put(plugin, new Pair<>(oldFileName, newFileName));
    }
    
    @SuppressWarnings("all")
    public void start() {
        ExecutorService es = Executors.newSingleThreadExecutor();
        try {
            es.submit(new Callable<Runnable>() {
                @Override
                public Runnable call() throws Exception {
                    plugin.getCoLogger().info("Updating jars...");
                    
                    files.forEach((p, k) -> {
                        try {
                            unload(p);
                            File original = new File(pluginFolder.getAbsolutePath(), k.getKey());
                            File old = new File(pluginFolder.getAbsolutePath(), k.getValue());
                            File nw = new File(updateFolder.getAbsolutePath(), k.getValue());
                            
                            original.delete();
                            Files.move(nw.toPath(), old.toPath(), StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
                        }
                        catch (NoSuchFieldException | IllegalAccessException | IOException e) {
                            e.printStackTrace();
                            plugin.getCoLogger().error("Failed to update jars.");
                        }
                    });
                    return (Runnable) () -> plugin.getCoLogger().info("Updates completed!");
                }
            }).get().run();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        catch (ExecutionException e) {
            e.printStackTrace();
        }
        es.shutdown();
    }
    
    @SuppressWarnings("unchecked")
    private static void unload(Plugin plugin) throws NoSuchFieldException, IllegalAccessException, IOException {
    
        String name = plugin.getName();
    
        PluginManager pluginManager = Bukkit.getPluginManager();
    
        SimpleCommandMap commandMap = null;
    
        List<Plugin> plugins = null;
    
        Map<String, Plugin> names = null;
        Map<String, Command> commands = null;
        Map<Event, SortedSet<RegisteredListener>> listeners = null;
    
        boolean reloadlisteners = true;
    
        if (pluginManager != null) {
        
            pluginManager.disablePlugin(plugin);
        
            Field pluginsField = Bukkit.getPluginManager().getClass().getDeclaredField("plugins");
            pluginsField.setAccessible(true);
            plugins = (List<Plugin>)pluginsField.get(pluginManager);
        
            Field lookupNamesField = Bukkit.getPluginManager().getClass().getDeclaredField("lookupNames");
            lookupNamesField.setAccessible(true);
            names = (Map<String, Plugin>)lookupNamesField.get(pluginManager);
        
            try {
                Field listenersField = Bukkit.getPluginManager().getClass().getDeclaredField("listeners");
                listenersField.setAccessible(true);
                listeners = (Map<Event, SortedSet<RegisteredListener>>)listenersField.get(pluginManager);
            }
            catch (Exception e) {
                reloadlisteners = false;
            }
        
            Field commandMapField = Bukkit.getPluginManager().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            commandMap = (SimpleCommandMap)commandMapField.get(pluginManager);
        
            Field knownCommandsField = SimpleCommandMap.class.getDeclaredField("knownCommands");
            knownCommandsField.setAccessible(true);
            commands = (Map<String, Command>)knownCommandsField.get(commandMap);
    
            pluginManager.disablePlugin(plugin);
        }
    
        if (plugins != null && plugins.contains(plugin)) {
            plugins.remove(plugin);
        }
    
        if (names != null && names.containsKey(name)) {
            names.remove(name);
        }
    
        if (listeners != null && reloadlisteners) {
            for (SortedSet<RegisteredListener> set : listeners.values()) {
                set.removeIf(value -> value.getPlugin() == plugin);
            }
        }
    
        if (commandMap != null) {
            for (Iterator<Map.Entry<String, Command>> it = commands.entrySet().iterator(); it.hasNext(); ) {
                Map.Entry<String, Command> entry = it.next();
                if (entry.getValue() instanceof PluginCommand) {
                    PluginCommand c = (PluginCommand)entry.getValue();
                    if (c.getPlugin() == plugin) {
                        c.unregister(commandMap);
                        it.remove();
                    }
                }
            }
        }
    
        // Attempt to close the classloader to unlock any handles on the plugin's jar file.
        ClassLoader cl = plugin.getClass().getClassLoader();
    
        if (cl instanceof URLClassLoader) {
        
        
            Field pluginField = cl.getClass().getDeclaredField("plugin");
            pluginField.setAccessible(true);
            pluginField.set(cl, null);
        
            Field pluginInitField = cl.getClass().getDeclaredField("pluginInit");
            pluginInitField.setAccessible(true);
            pluginInitField.set(cl, null);
        
        
            ((URLClassLoader)cl).close();
        
        }
    
        // Will not work on processes started with the -XX:+DisableExplicitGC flag, but lets try it anyway.
        // This tries to get around the issue where Windows refuses to unlock jar files that were previously loaded into the JVM.
        System.gc();
    }
}
