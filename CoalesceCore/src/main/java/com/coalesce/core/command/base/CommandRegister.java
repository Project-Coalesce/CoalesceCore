package com.coalesce.core.command.base;

import com.coalesce.core.plugin.ICoPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("WeakerAccess")
public abstract class CommandRegister<C extends CommandContext, T extends TabContext, B extends CommandBuilder> extends Command implements PluginIdentifiableCommand {
    
    protected final ICoPlugin plugin;
    protected final ProcessedCommand<C, T, B> command;
    
    public CommandRegister(ProcessedCommand<C, T, B> command, ICoPlugin plugin) {
        super(command.getName());
        this.command = command;
        this.plugin = plugin;
        this.description = command.getDescription();
        this.usageMessage = command.getUsage();
        this.setAliases(command.getAliases() != null ? new ArrayList<>(command.getAliases()) : new ArrayList<>());
    }
    
    @Override
    public abstract boolean execute(CommandSender sender, String alias, String[] args);
    
    @Override
    public abstract List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException;
    
    @Override
    public Plugin getPlugin() {
        return plugin;
    }

}
