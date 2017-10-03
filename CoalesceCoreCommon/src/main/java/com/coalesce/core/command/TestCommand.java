package com.coalesce.core.command;

import com.coalesce.core.SenderType;
import com.coalesce.core.command.annotation.Alias;
import com.coalesce.core.command.annotation.Command;
import com.coalesce.core.command.annotation.Completion;
import com.coalesce.core.command.annotation.Permission;
import com.coalesce.core.command.annotation.Sender;
import com.coalesce.core.command.base.CommandContext;
import com.coalesce.core.command.base.TabContext;

public final class TestCommand {
	
	@Alias({"tst","ts"})
	@Permission({"core.test", "core.test2"})
	@Sender({SenderType.PLAYER, SenderType.CONSOLE})
	@Command(name = "test", desc = "A test command", usage = "/test", max = 1, min = 0)
	public void testCommand(CommandContext context) {
		context.getSender().pluginMessage(context.joinArgs());
	}
	
	@Completion("test")
	public void testCompletion(TabContext context) {
		context.completionAt(0, "hello");
	}
	
}
