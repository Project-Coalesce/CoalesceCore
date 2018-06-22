package com.coalesce.core.command.defaults;

import com.coalesce.core.command.base.CommandRegister;
import com.coalesce.core.i18n.Translatable;
import com.coalesce.core.wrappers.CoSender;
import org.bukkit.command.CommandSender;

import java.util.List;

@SuppressWarnings("WeakerAccess")
public class DefaultCommandRegister<M extends Enum & Translatable> extends CommandRegister<DefaultCContext<M>, DefaultTContext<M>, M, DefaultCommandBuilder<M>, DefaultProcessedCommand<M>> {
    
    public DefaultCommandRegister(DefaultProcessedCommand<M> command) {
        super(command, command.getPlugin());
    }
    
    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {
        return command.run(new DefaultCContext<>(new CoSender(plugin, sender), alias, args, plugin));
    }
    
    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        return command.complete(new DefaultTContext<>(new DefaultCContext<>(new CoSender(plugin, sender), alias, args, plugin), command, args));
    }
}
