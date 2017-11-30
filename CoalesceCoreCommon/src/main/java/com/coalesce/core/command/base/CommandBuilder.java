package com.coalesce.core.command.base;

import com.coalesce.core.SenderType;
import com.coalesce.core.command.builder.interfaces.CommandExecutor;
import com.coalesce.core.command.builder.interfaces.TabExecutor;
import com.coalesce.core.plugin.ICoPlugin;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class CommandBuilder {
	
	private ProcessedCommand command;
	
	/**
	 * Creates a new CommandBuilder
	 * @param plugin The plugin the command is registered to
	 * @param name The name of the command
	 */
	public CommandBuilder(ICoPlugin plugin, String name) {
		this.command = new ProcessedCommand(plugin, name);
	}
	
	/**
	 * The method the command needs to run.
	 * @param executor The method of this command. Should be this::method_name
	 */
	public CommandBuilder executor(CommandExecutor executor) {
		command.setCommandExecutor(executor);
		return this;
	}
	
	/**
	 * The method the command needs to create a tab completion.
	 * @param executor The method of this command's tab completer
	 */
	public CommandBuilder completer(TabExecutor executor) {
		command.setTabExecutor(executor);
		return this;
	}
	
	/**
	 * The list of aliases for this command.
	 * @param aliases List of aliases.
	 */
	public CommandBuilder aliases(String... aliases) {
		command.setAliases(Stream.of(aliases).map(String::toLowerCase).collect(Collectors.toSet()));
		return this;
	}
	
	/**
	 * The list of aliases for this command.
	 * @param aliases List of aliases.
	 */
	public CommandBuilder aliases(Set<String> aliases) {
		command.setAliases(aliases);
		return this;
	}
	
	/**
	 * The command description.
	 * @param description The command description.
	 */
	public CommandBuilder description(String description) {
		command.setDescription(description);
		return this;
	}
	
	/**
	 * The command usage.
	 * @param usage The command usage.
	 */
	public CommandBuilder usage(String usage) {
		command.setUsage(usage);
		return this;
	}
	
	/**
	 * The needed permissions for this command.
	 * @param permission The required permission node.
	 */
	public CommandBuilder permission(String permission) {
		command.setPermission(permission);
		return this;
	}
	
	/**
	 * Minimum amount of arguments allowed in a command without throwing an error.
	 * @param minArgs The min amount of args without error.
	 */
	public CommandBuilder minArgs(int minArgs) {
		command.setMin(minArgs);
		return this;
	}
	
	/**
	 * Sets the maximum amount of arguments allowed in a command without throwing an error.
	 * @param maxArgs The max amount of args without error.
	 */
	public CommandBuilder maxArgs(int maxArgs) {
		command.setMax(maxArgs);
		return this;
	}
	
	/**
	 * Sets the sender types allowed to run this command
	 * @param allowedSenders The senders allowed to run the command
	 */
	public CommandBuilder senders(SenderType... allowedSenders) {
		command.setSenders(allowedSenders);
		return this;
	}
	
	/**
	 * This builds the ProcessedCommand
	 * @return A new ProcessedCommand.
	 */
	public ProcessedCommand build() {
		return command;
	}
	
}
