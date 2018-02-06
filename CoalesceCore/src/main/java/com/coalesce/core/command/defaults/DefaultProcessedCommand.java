package com.coalesce.core.command.defaults;

import com.coalesce.core.command.base.CommandContext;
import com.coalesce.core.command.base.ProcessedCommand;
import com.coalesce.core.command.base.TabContext;
import com.coalesce.core.plugin.ICoPlugin;

public class DefaultProcessedCommand extends ProcessedCommand<CommandContext, TabContext, DefaultCommandBuilder> {
    
    public DefaultProcessedCommand(ICoPlugin plugin, String name) {
        super(plugin, name);
    }
}
