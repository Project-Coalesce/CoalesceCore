package com.coalesce.core.command.base;

import com.coalesce.core.SenderType;
import com.coalesce.core.command.builder.interfaces.CommandExecutor;
import com.coalesce.core.command.builder.interfaces.TabExecutor;
import com.coalesce.core.plugin.ICoPlugin;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings({"unused", "unchecked"})
public abstract class CommandBuilder<C extends CommandContext, T extends TabContext, B extends CommandBuilder<C, T, B, P>, P extends ProcessedCommand<C, T, B>> {
    
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
    public B executor(CommandExecutor<C> executor) {
        command.setCommandExecutor(executor);
        return (B)this;
    }
    
    /**
     * The method the command needs to create a tab completion.
     *
     * @param executor The method of this command's tab completer
     */
    public B completer(TabExecutor<T> executor) {
        command.setTabExecutor(executor);
        return (B)this;
    }
    
    /**
     * The list of aliases for this command.
     *
     * @param aliases List of aliases.
     */
    public B aliases(String... aliases) {
        command.setAliases(Stream.of(aliases).map(String::toLowerCase).collect(Collectors.toSet()));
        return (B)this;
    }
    
    /**
     * The list of aliases for this command.
     *
     * @param aliases List of aliases.
     */
    public B aliases(Set<String> aliases) {
        command.setAliases(aliases);
        return (B)this;
    }
    
    /**
     * The command description.
     *
     * @param description The command description.
     */
    public B description(String description) {
        command.setDescription(description);
        return (B)this;
    }
    
    /**
     * The command usage.
     *
     * @param usage The command usage.
     */
    public B usage(String usage) {
        command.setUsage(usage);
        return (B)this;
    }
    
    /**
     * The needed permissions for this command.
     *
     * @param permission The required permission node.
     */
    public B permission(String... permission) {
        command.setPermission(permission);
        return (B)this;
    }
    
    /**
     * Minimum amount of arguments allowed in a command without throwing an error.
     *
     * @param minArgs The min amount of args without error.
     */
    public B minArgs(int minArgs) {
        command.setMin(minArgs);
        return (B)this;
    }
    
    /**
     * Sets the maximum amount of arguments allowed in a command without throwing an error.
     *
     * @param maxArgs The max amount of args without error.
     */
    public B maxArgs(int maxArgs) {
        command.setMax(maxArgs);
        return (B)this;
    }
    
    /**
     * Sets the sender types allowed to run this command
     *
     * @param allowedSenders The senders allowed to run the command
     */
    public B senders(SenderType... allowedSenders) {
        command.setSenders(allowedSenders);
        return (B)this;
    }
    
    /**
     * This builds the ProcessedCommand
     *
     * @return A new ProcessedCommand.
     */
    public abstract P build();
    
}
