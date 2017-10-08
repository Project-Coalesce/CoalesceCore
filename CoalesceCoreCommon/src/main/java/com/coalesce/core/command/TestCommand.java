package com.coalesce.core.command;

import com.coalesce.core.SenderType;
import com.coalesce.core.command.annotation.Alias;
import com.coalesce.core.command.annotation.Command;
import com.coalesce.core.command.annotation.Completion;
import com.coalesce.core.command.annotation.Permission;
import com.coalesce.core.command.annotation.Sender;
import com.coalesce.core.command.base.CommandContext;
import com.coalesce.core.command.base.TabContext;
import com.coalesce.core.config.YmlConfig;

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
	
	@Command(name = "ct", desc = "Tests the plugin config api", usage = "/ct", min = 3)
	public void configTest(CommandContext context) {
		YmlConfig config = new YmlConfig(context.argAt(0), context.getPlugin());
		config.addEntry(context.argAt(1), context.joinArgs(2));
	}
	
}
