package com.coalesce.core.bukkit;

import com.coalesce.core.command.base.CommandInfo;
import com.coalesce.core.plugin.ICoPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public final class CommandRegister extends Command implements PluginIdentifiableCommand {
    
    private final ICoPlugin plugin;
    private final CommandInfo command;
    
    public CommandRegister(CommandInfo commandInfo, ICoPlugin plugin) {
        super(commandInfo.getName());
        this.command = commandInfo;
        this.plugin = plugin;
        this.description = commandInfo.getDesc();
        this.usageMessage = commandInfo.getUsage();
        this.setAliases(commandInfo.getAliases() != null ? new ArrayList<>(commandInfo.getAliases()) : new ArrayList<>());
    }
    
    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        return command.run(new BukkitSender(commandSender, plugin), strings);
    }
    
    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        return command.complete(new BukkitSender(sender, plugin), args);
    }
    
    @Override
    public Plugin getPlugin() {
        return (CoPlugin)plugin;
    }
}
