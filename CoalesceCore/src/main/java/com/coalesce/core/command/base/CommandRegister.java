package com.coalesce.core.command.base;

import com.coalesce.core.i18n.Translatable;
import com.coalesce.core.plugin.ICoPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

//holy mother of generics
@SuppressWarnings("WeakerAccess")
public abstract class CommandRegister<C extends CommandContext<C, T, M, B, P>, T extends TabContext<C, T, M, B, P>, M extends Enum & Translatable, B extends CommandBuilder<C, T, M, B, P>, P extends ProcessedCommand<C, T, M, B, P>> extends Command implements PluginIdentifiableCommand {
    
    protected final ICoPlugin<M> plugin;
    protected final P command;
    
    public CommandRegister(P command, ICoPlugin<M> plugin) {
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
