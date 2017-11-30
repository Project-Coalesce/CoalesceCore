package com.coalesce.core.command.base;

import com.coalesce.core.SenderType;
import com.coalesce.core.command.builder.interfaces.CommandExecutor;
import com.coalesce.core.command.builder.interfaces.TabExecutor;
import com.coalesce.core.plugin.ICoPlugin;

import java.util.Set;

public final class ProcessedCommand {
	
	private CommandExecutor commandExecutor;
	private TabExecutor tabExecutor;
	private String description = "";
	private final ICoPlugin plugin;
	private SenderType[] senders;
	private Set<String> aliases;
	private String permission;
	private final String name;
	private String usage = "";
	private int min = -1;
	private int max = -1;
	
	public ProcessedCommand(ICoPlugin plugin, String name) {
		this.name = name;
		this.plugin = plugin;
	}
	
	/**
	 * The ProcessedCommand Builder
	 * @param plugin The plugin registering the command
	 * @param name The name of the command
	 * @return The command builder
	 */
	public static CommandBuilder builder(ICoPlugin plugin, String name) {
		return new CommandBuilder(plugin, name);
	}
	
	/**
	 * Gets the command Executor for this command.
	 * @return The command executor
	 */
	public CommandExecutor getCommandExecutor() {
		return commandExecutor;
	}
	
	/**
	 * Sets the executor of this command.(via method reference)
	 * @param commandExecutor The command executor
	 */
	public void setCommandExecutor(CommandExecutor commandExecutor) {
		this.commandExecutor = commandExecutor;
	}
	
	/**
	 * The tab executor for this command.
	 * @return The command tab Completer.
	 */
	public TabExecutor getTabExecutor() {
		return tabExecutor;
	}
	
	/**
	 * Sets the tab executor method for this command.
	 * @param tabExecutor The tab executor.
	 */
	public void setTabExecutor(TabExecutor tabExecutor) {
		this.tabExecutor = tabExecutor;
	}
	
	/**
	 * Gets the description of this command.
	 * @return The command description.
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Sets the description of this command.
	 * @param description The description of this command.
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * Gets the usage of this command.
	 * @return The command usage.
	 */
	public String getUsage() {
		return usage;
	}
	
	/**
	 * Sets the usage of this command.
	 * @param usage The command usage of this command.
	 */
	public void setUsage(String usage) {
		this.usage = usage;
	}
	
	/**
	 * Gets the permission needed to run this command
	 * @return The permission required
	 */
	public String getPermission() {
		return permission;
	}
	
	/**
	 * Sets the permission needed for this command
	 * @param permission The permission needed for this command.
	 */
	public void setPermission(String permission) {
		this.permission = permission;
	}
	
	/**
	 * Gets the minimum amount of allowed arguments for this command.
	 * @return The minimum amount of arguments.
	 */
	public int getMin() {
		return min;
	}
	
	/**
	 * Sets the minimum amount of arguments for this command before throwing an error.
	 * @param min The minimum amount of arguments without throwing an error.
	 */
	public void setMin(int min) {
		this.min = min;
	}
	
	/**
	 * The maximum amount of arguments allowed in this command before throwing an error..
	 * @return The maximum amount of allowed arguments.
	 */
	public int getMax() {
		return max;
	}
	
	/**
	 * Sets the maximum amount of arguments for this command before sending an error.
	 * @param max The maximum amount of arguments allowed for this command. (Default is -1 for no limit)
	 */
	public void setMax(int max) {
		this.max = max;
	}
	
	/**
	 * A set of aliases for this command.
	 * @return The alias set for this command.
	 */
	public Set<String> getAliases() {
		return aliases;
	}
	
	/**
	 * Sets the aliases for this command.
	 * @param aliases All the aliases for this command.
	 */
	public void setAliases(Set<String> aliases) {
		this.aliases = aliases;
	}
	
	/**
	 * Gets all the senders that can run this command
	 * @return The sender types allowed to run the command
	 */
	public SenderType[] getSenders() {
		return senders;
	}
	
	/**
	 * Sets the senders allowed to run this command
	 * @param senders The senders to allow
	 */
	public void setSenders(SenderType[] senders) {
		this.senders = senders;
	}
	
	/**
	 * The name of this command
	 * @return The name of this command.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Returns the plugin this command belongs too.
	 * @return The host plugin
	 */
	public ICoPlugin getPlugin() {
		return plugin;
	}
}
