package com.coalesce.core.command.defaults;

import com.coalesce.core.command.base.CommandBuilder;
import com.coalesce.core.command.base.CommandContext;
import com.coalesce.core.command.base.ProcessedCommand;
import com.coalesce.core.command.base.TabContext;
import com.coalesce.core.plugin.ICoPlugin;

public final class DefaultCommandBuilder extends CommandBuilder<CommandContext, TabContext, DefaultCommandBuilder, ProcessedCommand<CommandContext, TabContext, DefaultCommandBuilder>>{
    
    /**
     * Creates a new CommandBuilder
     *
     * @param plugin The plugin the command is registered to
     * @param name   The name of the command
     */
    public DefaultCommandBuilder(ICoPlugin plugin, String name) {
        super(plugin, name, new DefaultProcessedCommand(plugin, name));
    }
}
