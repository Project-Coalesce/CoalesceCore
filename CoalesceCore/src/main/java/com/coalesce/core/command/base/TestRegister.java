package com.coalesce.core.command.base;

import com.coalesce.core.wrappers.CoSender;
import org.bukkit.command.CommandSender;

import java.util.List;

public class TestRegister extends CommandRegister<TestCommandContext, TestTabContext>{
    
    public TestRegister(ProcessedCommand<TestCommandContext, TestTabContext> command) {
        super(command, command.getPlugin());
    }
    
    @Override
    public boolean execute(CommandSender commandSender, String s, String[] args) {
        return command.run(new TestCommandContext(new CoSender(plugin, commandSender), args, plugin));
    }
    
    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        return command.complete(new TestTabContext(new TestCommandContext(new CoSender(plugin, sender), args, plugin), command, args));
    }
}
