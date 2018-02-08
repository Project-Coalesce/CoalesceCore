package com.coalesce.core.command.base;

import com.coalesce.core.SenderType;
import com.coalesce.core.command.builder.interfaces.TabExecutor;
import com.coalesce.core.wrappers.CoSender;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@SuppressWarnings({"WeakerAccess", "unused", "unchecked"})
public class TabContext<C extends CommandContext, T extends TabContext, B extends CommandBuilder> {
    
    private final ProcessedCommand<C, T, B> command;
    private final List<String> possible;
    private final CoSender sender;
    private final String[] args;
    private final C context;
    
    public TabContext(C context, ProcessedCommand<C, T, B> command, String[] args) {
        this.possible = new ArrayList<>();
        this.sender = context.getSender();
        this.command = command;
        this.context = context;
        this.args = args;
    }
    
    /**
     * Gets the current alias being used to run this command
     * @return The alias used
     */
    public String getAlias() {
        return context.getAlias();
    }
    
    /**
     * Checks if the sender has a specific permission
     *
     * @param permission The permission to check for
     * @return True if the sender has permission.
     */
    public boolean hasPermission(String permission) {
        return sender.hasPermission(permission);
    }
    
    /**
     * Checks if the sender of this command has any of these permissions
     *
     * @param permission The permissions to look for
     * @return True if the sender has at least one of the permissions specified
     */
    public boolean hasAnyPermission(String... permission) {
        return sender.hasAnyPermission(permission);
    }
    
    /**
     * Checks if the sender of this command has all permissions
     *
     * @param permissions The permissions the sender needs
     * @return True if the sender has all the permissions, false otherwise
     */
    public boolean hasAllPermissions(String... permissions) {
        return sender.hasAllPermissions(permissions);
    }
    
    /**
     * Gets the command context of the command being executed
     *
     * @return The command context
     */
    public CommandContext getCommandContext() {
        return context;
    }
    
    /**
     * Gets the command info of the command being executed
     *
     * @return The command info
     */
    public ProcessedCommand getCommand() {
        return command;
    }
    
    /**
     * Gets the CoSender that's executing the command.
     *
     * @return The command sender.
     */
    public CoSender getSender() {
        return sender;
    }
    
    /**
     * Gets how many args there are.
     *
     * @return The amount of args.
     */
    public int getLength() {
        return args.length - 1;
    }
    
    /**
     * Checks if the arg length equals another length
     *
     * @param length The length to look for.
     * @return True if the length matches
     */
    public boolean length(int length) {
        return getLength() == length;
    }
    
    /**
     * Gets the previous argument in the command
     *
     * @return The previous arg
     */
    public String getPrevious() {
        return getLength() == 0 ? null : (String)context.getArgs().get(getLength() - 1);
    }
    
    /**
     * Checks if the previous arg equals another string.
     *
     * @param previousArg The arg to look for.
     * @return True if the previous arg matches the provided
     */
    public boolean previous(String previousArg) {
        return getPrevious() != null && getPrevious().matches(previousArg);
    }
    
    /**
     * A quick player completion for tab completes.
     *
     * @param index The index to run this completion at.
     */
    public void playerCompletion(int index) {
        if (length(index)) {
            possible.clear();
            possible.addAll(Bukkit.getServer().getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toSet()));
        }
    }
    
    /**
     * A quick tab complete method for any point in the command
     *
     * @param completions The completions to use
     */
    public void completion(String... completions) {
        possible.clear();
        possible.addAll(Arrays.asList(completions));
    }
    
    /**
     * A quick tab complete method for a specific index in the command
     *
     * @param index       The index for this tab completion to take place
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
     *
     * @param previousText The previous string to look for
     * @param completions  The completions to use
     */
    public void completionAfter(String previousText, String... completions) {
        if (previous(previousText)) {
            possible.clear();
            possible.addAll(Arrays.asList(completions));
        }
    }
    
    /**
     * Gets the current argument being typed
     *
     * @return The current arg being typed
     */
    public String getCurrent() {
        return context.argAt(getLength());
    }
    
    /**
     * Gets an argument at a specific index
     *
     * @param index The index to get the arg from
     * @return The arg, null if it doesn't exist.
     */
    public String argAt(int index) {
        return context.argAt(index);
    }
    
    /**
     * Runs a sub completion of this completion if the sender matches the given sender type
     *
     * @param senderType The type of sender needed to run the subCompletion
     * @param executor   The completion method (method reference)
     * @return true if the senderType passed. False otherwise
     */
    public boolean subCompletion(SenderType senderType, TabExecutor<T> executor) {
        if (getSender().getType() == senderType) {
            executor.run((T)this);
            return true;
        }
        return false;
    }
    
    /**
     * Runs a sub completion of this completion if the index of the completion matches the given index
     *
     * @param index    The index needed to run the completion
     * @param executor The completion method (method reference)
     * @return true if the index passed. False otherwise.
     */
    public boolean subCompletionAt(int index, TabExecutor<T> executor) {
        if (getLength() == index) {
            executor.run((T)this);
            return true;
        }
        return false;
    }
    
    /**
     * Runs a sub completion of this completion if the index matches the given index and if the sender matches the given sender type
     *
     * @param index      The index needed to run the completion
     * @param senderType The type of sender needed to run the subCompletion
     * @param executor   The completion method (method reference)
     * @return true if the index and the senderType passed. False otherwise
     */
    public boolean subCompletionAt(int index, SenderType senderType, TabExecutor<T> executor) {
        if (getLength() == index && getSender().getType() == senderType) {
            executor.run((T)this);
            return true;
        }
        return false;
    }
    
    /**
     * Runs a sub completion of this completion if the predicate passes.
     *
     * @param predicate The predicate needed to pass
     * @param executor  The completion method (method reference)
     * @return true if the predicate passed. False otherwise
     */
    public boolean subCompletion(Predicate<TabContext<C, T, B>> predicate, TabExecutor<T> executor) {
        if (predicate.test(this)) {
            executor.run((T)this);
            return true;
        }
        return false;
    }
    
    
    List<String> currentPossibleCompletion() {
        return possible;
    }
    
}
