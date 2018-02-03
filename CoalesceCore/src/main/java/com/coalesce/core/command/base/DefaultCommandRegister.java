package com.coalesce.core.command.base;

import com.coalesce.core.wrappers.CoSender;
import org.bukkit.command.CommandSender;

import java.util.List;

@SuppressWarnings("WeakerAccess")
public class DefaultCommandRegister extends CommandRegister<CommandContext, TabContext> {
    
    private CommandContext<CommandContext, TabContext> commandContext;
    
    public DefaultCommandRegister(ProcessedCommand<CommandContext, TabContext> command) {
        super(command, command.getPlugin());
    }
    
    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {
        this.commandContext = new CommandContext<>(new CoSender(plugin, sender), args, plugin);
        return command.run(commandContext);
    }
    
    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        if (commandContext == null) commandContext = new CommandContext<>(new CoSender(plugin, sender), args, plugin);
        return command.complete(new TabContext<>(commandContext, command, args));
    }
}
