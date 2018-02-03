package com.coalesce.core.command.builder.interfaces;

import com.coalesce.core.command.base.CommandContext;

@FunctionalInterface
public interface CommandExecutor<T extends CommandContext> {
    
    /**
     * Runs the command method
     *
     * @param context The command context
     */
    void run(T context);
    
}
