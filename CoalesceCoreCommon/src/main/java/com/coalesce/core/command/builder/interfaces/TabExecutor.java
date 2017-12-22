package com.coalesce.core.command.builder.interfaces;

import com.coalesce.core.command.base.TabContext;

@FunctionalInterface
public interface TabExecutor {
    
    /**
     * Runs the tab completion method
     *
     * @param context The tab completion context
     */
    void run(TabContext context);
    
}
