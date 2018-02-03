package com.coalesce.core.command.base;

import com.coalesce.core.Color;
import com.coalesce.core.plugin.ICoPlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({"unused", "WeakerAccess", "u"})
public class CommandStore {
    
    private final Map<String, ProcessedCommand<? extends CommandContext, ? extends TabContext>> commandMap;
    private final ICoPlugin plugin;
    private CommandMap bukkitCommandMap;
    
    
    public CommandStore(ICoPlugin plugin) {
        this.plugin = plugin;
        this.commandMap = new HashMap<>();
    
        try {
        
            Field field = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            field.setAccessible(true);
            this.bukkitCommandMap = (CommandMap)field.get(Bukkit.getServer());
        }
    
        catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        
    }
    
    /**
     * Registers a command into the command map.
     *
     * @param command The command info.
     */
    public <C extends CommandContext, T extends TabContext, R extends CommandRegister<C, T>> void registerCommand(ProcessedCommand<C, T> command, R register) {
        if (this.bukkitCommandMap == null) {
            throw new RuntimeException("Bukkit CommandMap could not be found");
        }
        if (!isRegistered(command.getName())) {
            bukkitCommandMap.register(Color.stripColor(plugin.getDisplayName()), register);
        }
    }
    
    public void registerCommand(ProcessedCommand<CommandContext, TabContext> command) {
        registerCommand(command, new DefaultCommandRegister(command));
    }
    
    /**
     * Checks if the command is registered
     *
     * @param commandName The command name
     * @return True if the command is registered, false otherwise.
     */
    public boolean isRegistered(String commandName) {
        if (this.bukkitCommandMap == null) {
            throw new RuntimeException("Bukkit CommandMap could not be found.");
        }
        return this.bukkitCommandMap.getCommand(commandName) != null;
    }
    
    /**
     * Gets a command from the plugins command map.
     *
     * @param name The name of the command to get.
     * @return The command if exists.
     */
    public ProcessedCommand<? extends CommandContext, ? extends TabContext> getCommand(String name) {
        return commandMap.get(name);
    }
}