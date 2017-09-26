package com.coalesce.core.command.base;

import com.coalesce.core.Sender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class TabContext {
	private final CommandContext context;
	private final List<String> possible;
	private final CommandInfo command;
	private final Sender sender;
	private final String[] args;
	
	public TabContext(CommandContext context, CommandInfo command, Sender sender, String[] args) {
		this.possible = new ArrayList<>();
		this.command = command;
		this.context = context;
		this.sender = sender;
		this.args = args;
	}
	
	/**
	 * Gets the command context of the command being executed
	 * @return The command context
	 */
	public CommandContext getCommandContext() {
		return context;
	}
	
	/**
	 * Gets the command info of the command being executed
	 * @return The command info
	 */
	public CommandInfo getCommand() {
		return command;
	}
	
	/**
	 * Gets the Sender that's executing the command.
	 * @return The command sender.
	 */
	public Sender getSender() {
		return sender;
	}
	
	/**
	 * Gets how many args there are.
	 * @return The amount of args.
	 */
	public int getLength() {
		return args.length - 1;
	}
	
	/**
	 * Checks if the arg length equals another length
	 * @param length The length to look for.
	 * @return True if the length matches
	 */
	public boolean length(int length) {
		return getLength() == length;
	}
	
	/**
	 * Gets the previous argument in the command
	 * @return The previous arg
	 */
	public String getPrevious() {
		return context.getArgs().get(getLength());
	}
	
	/**
	 * Checks if the previous arg equals another string.
	 * @param previousArg The arg to look for.
	 * @return True if the previous arg matches the provided
	 */
	public boolean previous(String previousArg) {
		return getPrevious().matches(previousArg);
	}
	
	/**
	 * A quick player completion for tab completes.
	 * @param index The index to run this completion at
	 * @param predicate A filter of the players
	 */
/*	public void playerCompletion(int index, Predicate<? super Player> predicate) {
		if (length(index)) {
			possible.clear();
			Kernel.getPlayers().stream().filter(predicate).forEach(p -> possible.add(p.getName()));
		}
	}*/
	
	/**
	 * A quick player completion for tab completes.
	 * @param index The index to run this completion at.
	 */
/*	public void playerCompletion(int index) {
		if (length(index)) {
			possible.clear();
			Kernel.getPlayers().forEach(p -> possible.add(p.getName()));
		}
	}*/
	
	/**
	 * A quick tab complete method for any point in the command
	 * @param completions The completions to use
	 */
	public void completion(String... completions) {
		possible.clear();
		possible.addAll(Arrays.asList(completions));
	}
	
	/**
	 * A quick tab complete method for a specific index in the command
	 * @param index The index for this tab completion to take place
	 * @param completions The completions to use
	 */
	public void completionAt(int index, String... completions) {
		if (length(index)) {
			possible.clear();
			possible.addAll(Arrays.asList(completions));
		}
	}
	
	/**
	 * A quick tab complete method for use after a specific keyword
	 * @param previousText The previous string to look for
	 * @param completions The completions to use
	 */
	public void completionAfter(String previousText, String... completions) {
		if (previous(previousText)) {
			possible.clear();
			possible.addAll(Arrays.asList(completions));
		}
	}
	
	List<String> currentPossibleCompletion() {
		return possible;
	}
	
}
