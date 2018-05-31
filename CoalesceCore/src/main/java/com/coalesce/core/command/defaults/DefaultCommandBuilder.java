package com.coalesce.core.command.defaults;

import com.coalesce.core.command.base.CommandBuilder;
import com.coalesce.core.i18n.Translatable;
import com.coalesce.core.plugin.ICoPlugin;

public final class DefaultCommandBuilder<M extends Enum & Translatable> extends CommandBuilder<DefaultCContext<M>, DefaultTContext<M>, M, DefaultCommandBuilder<M>, DefaultProcessedCommand<M>>{
    
    /**
     * Creates a new CommandBuilder
     *
     * @param plugin The plugin the command is registered to
     * @param name   The name of the command
     */
    public DefaultCommandBuilder(ICoPlugin<M> plugin, String name) {
        super(plugin, name, new DefaultProcessedCommand<>(plugin, name));
    }
    
    @Override
    public DefaultProcessedCommand<M> build() {
        return command;
    }
}
