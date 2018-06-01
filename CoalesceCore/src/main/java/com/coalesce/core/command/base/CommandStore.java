package com.coalesce.core.command.base;

import com.coalesce.core.Color;
import com.coalesce.core.command.defaults.DefaultCommandRegister;
import com.coalesce.core.command.defaults.DefaultProcessedCommand;
import com.coalesce.core.i18n.Translatable;
import com.coalesce.core.plugin.ICoPlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

@SuppressWarnings({"unused", "WeakerAccess"})
public final class CommandStore<M extends Enum & Translatable> {
    
    private final Map<String, ProcessedCommand<? extends CommandContext, ? extends TabContext, M, ? extends CommandBuilder, ? extends ProcessedCommand>> commandMap;
    private final ICoPlugin<M> plugin;
    private CommandMap bukkitCommandMap;
    
    
    public CommandStore(ICoPlugin<M> plugin) {
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
    public <C extends CommandContext<C, T, M, B, P>, T extends TabContext<C, T, M, B, P>, B extends CommandBuilder<C, T, M, B, P>, P extends ProcessedCommand<C, T, M, B, P>, R extends CommandRegister<C, T, M, B, P>> void registerCommand(ProcessedCommand<C, T, M, B, P> command, R register) {
        if (this.bukkitCommandMap == null) {
            throw new RuntimeException("Bukkit CommandMap could not be found");
        }
        if (!isRegistered(command.getName())) {
            bukkitCommandMap.register(Color.stripColor(plugin.getDisplayName()), register);
            commandMap.put(command.getName(), command);
        }
    }

    /**
     * Registers an array of commands
     * @param commands The commands to register
     */
    @SafeVarargs
    public final void registerCommands(DefaultProcessedCommand<M>... commands) {
        Stream.of(commands).forEach(this::registerCommand);
    }
    
    /**
     * Registers a command
     * @param command The command to register
     */
    @SuppressWarnings("unchecked")
    public void registerCommand(DefaultProcessedCommand<M> command) {
        registerCommand(command, new DefaultCommandRegister<>(command));
    }
    
    /**
     * Unregisters a command via name
     * @param name The name of the command to unregister
     */
    @SuppressWarnings("unchecked")
    public void unregisterCommand(String name) {
        try {
            Field commandField = bukkitCommandMap.getClass().getDeclaredField("knownCommands");
            commandField.setAccessible(true);
            Map<String, Command> commands = (Map<String, Command>)commandField.get(bukkitCommandMap);
            commands.remove(name);
            for (String alias : getCommand(name).getAliases()) {
                if (commands.containsKey(alias) && commands.get(alias).toString().contains(name)) {
                    commands.remove(alias);
                }
            }
            commandMap.remove(name);
        }
        catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
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
    public ProcessedCommand<? extends CommandContext, ? extends TabContext, M, ? extends CommandBuilder, ? extends ProcessedCommand> getCommand(String name) {
        return commandMap.get(name);
    }
}
