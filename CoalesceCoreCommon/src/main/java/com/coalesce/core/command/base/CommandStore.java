package com.coalesce.core.command.base;

import com.coalesce.core.command.annotation.Command;
import com.coalesce.core.command.annotation.Completion;
import com.coalesce.core.plugin.ICoPlugin;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public abstract class CommandStore {
	
	private final ICoPlugin plugin;
	private final Set<Object> registeredObjects;
	private final Map<String, CompletionInfo> completionMap;
	private final Map<String, CommandInfo> commandMap;
	
	public CommandStore(ICoPlugin plugin) {
		this.registeredObjects = new HashSet<>();
		this.completionMap = new HashMap<>();
		this.commandMap = new HashMap<>();
		this.plugin = plugin;
	}
	
	/**
	 * Registers a command into the command map.
	 * @param info The command info.
	 */
	protected abstract void registerCommand(CommandInfo info);
	
	/**
	 * Checks if the command is registered
	 * @param commandName The command name
	 * @return True if the command is registered, false otherwise.
	 */
	public abstract boolean isRegistered(String commandName);
	
	/**
	 * Gets a command from the plugins command map.
	 * @param name The name of the command to get.
	 * @return The command if exists.
	 */
	public CommandInfo getCommand(String name) {
		return commandMap.get(name);
	}
	
	/**
	 * Registers a command made with the CommandBuilder
	 * @param command The command to register
	 */
	public void registerCommand(ProcessedCommand command) {
		CommandInfo commandInfo = new CommandInfo(command, plugin);
		registerCommand(commandInfo);
	}
	
	/**
	 * Adds a command into the command map.
	 * @param commandObj The object the command is stored in.
	 */
	public void registerCommand(Object commandObj) {
		registeredObjects.add(commandObj);
	}
	
	/**
	 * Registers all the objects in the object map.
	 * <p>The final registering of commands</p>
	 */
	protected abstract void registerObjects();
	
	protected void register() {
		registeredObjects.forEach(o -> Stream.of(o.getClass().getDeclaredMethods()).forEach(m -> {
			if (m.isAnnotationPresent(Completion.class)) {
				CompletionInfo info = new CompletionInfo(o, m);
				for (String command : info.getCommands()) {
					completionMap.put(command, info);
				}
			}
		}));
		registeredObjects.forEach(o -> Stream.of(o.getClass().getDeclaredMethods()).forEach(m -> {
			if (m.isAnnotationPresent(Command.class)) {
				String name = m.getAnnotation(Command.class).name();
				CompletionInfo completion = null;
				if (completionMap.containsKey(name)) {
					completion = completionMap.get(name);
				}
				CommandInfo info = new CommandInfo(o, m, completion, plugin);
				commandMap.put(name, info);
				if (!isRegistered(name)) registerCommand(info);
			}
		}));
	}
	
}
