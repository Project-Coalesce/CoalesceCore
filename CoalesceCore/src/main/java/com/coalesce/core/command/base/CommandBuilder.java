package com.coalesce.core.command.base;

import com.coalesce.core.SenderType;
import com.coalesce.core.command.builder.interfaces.CommandExecutor;
import com.coalesce.core.command.builder.interfaces.TabExecutor;
import com.coalesce.core.plugin.ICoPlugin;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("unused")
public abstract class CommandBuilder<C extends CommandContext, T extends TabContext, P extends ProcessedCommand<C, T>> {
    
    protected P command;
    
    /**
     * Creates a new CommandBuilder
     *
     * @param plugin The plugin the command is registered to
     * @param name   The name of the command
     */
    public CommandBuilder(ICoPlugin plugin, String name, P command) {
        this.command = command;
    }
    
    /**
     * The method the command needs to run.
     *
     * @param executor The method of this command. Should be this::method_name
     */
    public CommandBuilder<C, T, P> executor(CommandExecutor<C> executor) {
        command.setCommandExecutor(executor);
        return this;
    }
    
    /**
     * The method the command needs to create a tab completion.
     *
     * @param executor The method of this command's tab completer
     */
    public CommandBuilder<C, T, P> completer(TabExecutor<T> executor) {
        command.setTabExecutor(executor);
        return this;
    }
    
    /**
     * The list of aliases for this command.
     *
     * @param aliases List of aliases.
     */
    public CommandBuilder<C, T, P> aliases(String... aliases) {
        command.setAliases(Stream.of(aliases).map(String::toLowerCase).collect(Collectors.toSet()));
        return this;
    }
    
    /**
     * The list of aliases for this command.
     *
     * @param aliases List of aliases.
     */
    public CommandBuilder<C, T, P> aliases(Set<String> aliases) {
        command.setAliases(aliases);
        return this;
    }
    
    /**
     * The command description.
     *
     * @param description The command description.
     */
    public CommandBuilder<C, T, P> description(String description) {
        command.setDescription(description);
        return this;
    }
    
    /**
     * The command usage.
     *
     * @param usage The command usage.
     */
    public CommandBuilder<C, T, P> usage(String usage) {
        command.setUsage(usage);
        return this;
    }
    
    /**
     * The needed permissions for this command.
     *
     * @param permission The required permission node.
     */
    public CommandBuilder<C, T, P> permission(String... permission) {
        command.setPermission(permission);
        return this;
    }
    
    /**
     * Minimum amount of arguments allowed in a command without throwing an error.
     *
     * @param minArgs The min amount of args without error.
     */
    public CommandBuilder<C, T, P> minArgs(int minArgs) {
        command.setMin(minArgs);
        return this;
    }
    
    /**
     * Sets the maximum amount of arguments allowed in a command without throwing an error.
     *
     * @param maxArgs The max amount of args without error.
     */
    public CommandBuilder<C, T, P> maxArgs(int maxArgs) {
        command.setMax(maxArgs);
        return this;
    }
    
    /**
     * Sets the sender types allowed to run this command
     *
     * @param allowedSenders The senders allowed to run the command
     */
    public CommandBuilder<C, T, P> senders(SenderType... allowedSenders) {
        command.setSenders(allowedSenders);
        return this;
    }
    
    /**
     * This builds the ProcessedCommand
     *
     * @return A new ProcessedCommand.
     */
    public P build() {
        return command;
    }
    
}
