package com.coalesce.core.command.defaults;

import com.coalesce.core.command.base.CommandContext;
import com.coalesce.core.command.base.CommandRegister;
import com.coalesce.core.command.base.ProcessedCommand;
import com.coalesce.core.command.base.TabContext;
import com.coalesce.core.wrappers.CoSender;
import org.bukkit.command.CommandSender;

import java.util.List;

@SuppressWarnings("WeakerAccess")
public final class DefaultCommandRegister extends CommandRegister<CommandContext, TabContext, DefaultCommandBuilder> {
    
    public DefaultCommandRegister(ProcessedCommand<CommandContext, TabContext, DefaultCommandBuilder> command) {
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
