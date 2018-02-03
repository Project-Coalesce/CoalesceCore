package com.coalesce.core.command.base;

import com.coalesce.core.wrappers.CoSender;

public class TestTabContext extends TabContext<TestCommandContext, TestTabContext> {
    
    public TestTabContext(TestCommandContext context, ProcessedCommand<TestCommandContext, TestTabContext> command, String[] args) {
        super(context, command, args);
    }
    
    public void hello() {
        System.out.println("hello");
    }
    
}
