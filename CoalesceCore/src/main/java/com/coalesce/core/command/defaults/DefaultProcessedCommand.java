package com.coalesce.core.command.defaults;

import com.coalesce.core.command.base.ProcessedCommand;
import com.coalesce.core.i18n.Translatable;
import com.coalesce.core.plugin.ICoPlugin;

public class DefaultProcessedCommand<M extends Enum & Translatable> extends ProcessedCommand<DefaultCContext<M>, DefaultTContext<M>, M, DefaultCommandBuilder<M>, DefaultProcessedCommand<M>> {
    
    public DefaultProcessedCommand(ICoPlugin<M> plugin, String name) {
        super(plugin, name);
    }
    
    /**
     * The ProcessedCommand Builder
     *
     * @param plugin The plugin registering the command
     * @param name   The name of the command
     * @return The command builder
     */
    public static <M extends Enum & Translatable> DefaultCommandBuilder<M> builder(ICoPlugin<M> plugin, String name) {
        return new DefaultCommandBuilder<>(plugin, name);
    }
    
}
