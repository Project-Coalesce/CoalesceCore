package com.coalesce.core.command.base;

import com.coalesce.core.wrappers.CoSender;
import org.bukkit.command.CommandSender;

import java.util.List;

@SuppressWarnings("WeakerAccess")
public class DefaultCommandRegister extends CommandRegister<CommandContext, TabContext> {
    
    public DefaultCommandRegister(ProcessedCommand<CommandContext, TabContext> command) {
        super(command, command.getPlugin());
    }
    
    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {
        return command.run(new CommandContext<>(new CoSender(plugin, sender), args, plugin));
    }
    
    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        return command.complete(new TabContext<>(new CommandContext<>(new CoSender(plugin, sender), args, plugin), command, args));
    }
}
