package com.coalesce.core.command;

import com.coalesce.core.Color;
import com.coalesce.core.SenderType;
import com.coalesce.core.command.base.CommandContext;
import com.coalesce.core.command.base.ProcessedCommand;
import com.coalesce.core.command.base.TabContext;
import com.coalesce.core.plugin.ICoPlugin;

public final class BuilderTest {
	
	public BuilderTest(ICoPlugin plugin) {
		
		ProcessedCommand command = ProcessedCommand.builder(plugin, "test2")
				.permission("core.test1")
				.executor(this::testCommand2)
				.completer(this::testCompletion)
				.minArgs(1)
				.senders(SenderType.PLAYER)
				.usage("/test")
				.description("tests the builder pattern command registration")
				.build();
		
		plugin.getCommandStore().registerCommand(command);
	}
	
	private void testCommand2(CommandContext context) {
		System.out.println(Color.toConsoleColor('&', context.joinArgs()));
	}
	
	private void testCompletion(TabContext context) {
		context.completion("hello", "hi", "ey");
	}
	
}
