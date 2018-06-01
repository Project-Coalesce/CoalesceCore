package com.coalesce.core.command.defaults;

import com.coalesce.core.command.base.TabContext;
import com.coalesce.core.i18n.Translatable;

public class DefaultTContext<M extends Enum & Translatable> extends TabContext<DefaultCContext<M>, DefaultTContext<M>, M, DefaultCommandBuilder<M>, DefaultProcessedCommand<M>> {
    
    public DefaultTContext(DefaultCContext<M> context, DefaultProcessedCommand<M> command, String[] args) {
        super(context, command, args);
    }
}
