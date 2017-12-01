package com.coalesce.core.command.base;

import com.coalesce.core.wrappers.CoSender;
import com.coalesce.core.plugin.ICoPlugin;

import java.util.Arrays;
import java.util.List;

public final class CommandContext {
	
	private ICoPlugin plugin;
	private final CoSender sender;
	private final List<String> args;
	
	public CommandContext(CoSender sender, String[] args, ICoPlugin plugin) {
		this.plugin = plugin;
		this.sender = sender;
		this.args = Arrays.asList(args);
	}
	
	/**
	 * Gets the plugin
	 * @return The plugin
	 */
	public ICoPlugin getPlugin() {
		return plugin;
	}
	
	/**
	 * Gets the command sender
	 * @return The command sender
	 */
	public
	CoSender getSender() {
		return sender;
	}
	
	/**
	 * Gets a list of arguments from the command
	 * @return The command arguments.
	 */
	public List<String> getArgs() {
		return args;
	}
	
	/**
	 * Checks if the command ran had arguments
	 * @return True if it had arguments
	 */
	public boolean hasArgs() {
		return !args.isEmpty();
	}
	
	/**
	 * Gets an argument at a specified index
	 * @param index The index to get the arg at.
	 * @return The arg at the specified index. If none exists its null.
	 */
	public String argAt(int index) {
		if (index < 0 || index >= args.size()) return null;
		return args.get(index);
	}
	
	/**
	 * Joins arguments from one index of the command to another.
	 * @param start The start index
	 * @param finish The end index
	 * @return One string combining the strings between the two index's
	 */
	public String joinArgs(int start, int finish) {
		if (args.isEmpty()) return "";
		return String.join(" ", args.subList(start, finish));
	}
	
	/**
	 * Joins all the arguments after a specific index.
	 * @param start Where to join the strings at
	 * @return One string combining all the strings after the index
	 */
	public String joinArgs(int start) {
		return joinArgs(start, args.size());
	}
	
	/**
	 * Joins all the command arguments together
	 * @return All the command arguments in one string
	 */
	public String joinArgs() {
		return joinArgs(0);
	}
	
	/**
	 * Sends a message to the player/console.
	 * @param message The message to send.
	 */
	public void send(String message) {
		sender.sendMessage(message);
	}
	
	/**
	 * Sends a formatted plguin message.
	 *
	 * <p>Ex. [PlguinName] MESSAGE FROM THE PARAMETER</p>
	 *
	 * @param message The message to send with the plugin name before it.
	 */
	public void pluginMessage(String message) {
		send(plugin.getCoFormatter().format(message));
	}
}
