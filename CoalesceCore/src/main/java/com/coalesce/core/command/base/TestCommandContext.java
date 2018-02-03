package com.coalesce.core.command.base;

import com.coalesce.core.plugin.ICoPlugin;
import com.coalesce.core.wrappers.CoSender;

public class TestCommandContext extends CommandContext<TestCommandContext, TestTabContext> {
    
    public TestCommandContext(CoSender sender, String[] args, ICoPlugin plugin) {
        super(sender, args, plugin);
    }
    
    public void hello() {
        System.out.println("Hello");
    }
}
