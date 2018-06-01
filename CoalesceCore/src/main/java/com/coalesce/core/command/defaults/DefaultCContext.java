package com.coalesce.core.command.defaults;

import com.coalesce.core.command.base.CommandContext;
import com.coalesce.core.i18n.Translatable;
import com.coalesce.core.plugin.ICoPlugin;
import com.coalesce.core.wrappers.CoSender;

public class DefaultCContext<M extends Enum & Translatable> extends CommandContext<DefaultCContext<M>, DefaultTContext<M>, M, DefaultCommandBuilder<M>, DefaultProcessedCommand<M>> {
    
    public DefaultCContext(CoSender sender, String alias, String[] args, ICoPlugin<M> plugin) {
        super(sender, alias, args, plugin);
    }
}
