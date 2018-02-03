package com.coalesce.core.command.base;

import com.coalesce.core.wrappers.CoSender;
import org.bukkit.command.CommandSender;

import java.util.List;

public class TestRegister extends CommandRegister<TestCommandContext, TestTabContext>{
    
    private TestCommandContext commandContext;
    
    public TestRegister(ProcessedCommand<TestCommandContext, TestTabContext> command) {
        super(command, command.getPlugin());
    }
    
    @Override
    public boolean execute(CommandSender commandSender, String s, String[] args) {
        this.commandContext = new TestCommandContext(new CoSender(plugin, commandSender), args, plugin);
        return command.run(commandContext);
    }
    
    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        return command.complete(new TestTabContext(commandContext, command, args));
    }
}
