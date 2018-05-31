package com.coalesce.core.command.builder.interfaces;

import com.coalesce.core.command.base.CommandBuilder;
import com.coalesce.core.command.base.CommandContext;
import com.coalesce.core.command.base.ProcessedCommand;
import com.coalesce.core.command.base.TabContext;
import com.coalesce.core.i18n.Translatable;

@FunctionalInterface
public interface CommandExecutor<C extends CommandContext<C, T, M, B, P>, T extends TabContext<C, T, M, B, P>, M extends Enum & Translatable, B extends CommandBuilder<C, T, M, B, P>, P extends ProcessedCommand<C, T, M, B, P>> {
    
    /**
     * Runs the command method
     *
     * @param context The command context
     */
    void run(C context);
    
}
