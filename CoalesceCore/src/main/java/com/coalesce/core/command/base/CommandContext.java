package com.coalesce.core.command.base;

import com.coalesce.core.Coalesce;
import com.coalesce.core.Color;
import com.coalesce.core.SenderType;
import com.coalesce.core.chat.CoFormatter;
import com.coalesce.core.command.builder.interfaces.CommandExecutor;
import com.coalesce.core.plugin.ICoPlugin;
import com.coalesce.core.text.Text;
import com.coalesce.core.wrappers.CoSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

@SuppressWarnings({"unused", "unchecked", "WeakerAccess"})
public class CommandContext<C extends CommandContext, T extends TabContext> {
    
    private ICoPlugin plugin;
    private final String alias;
    private final CoSender sender;
    private final List<String> args;
    
    public CommandContext(CoSender sender, String alias, String[] args, ICoPlugin plugin) {
        this.alias = alias;
        this.plugin = plugin;
        this.sender = sender;
        this.args = Arrays.asList(args);
    }
    
    /**
     * Gets the current alias being used to run this command
     * @return The alias used
     */
    public String getAlias() {
        return alias;
    }
    
    /**
     * Gets how long the command currently is.
     *
     * @return How many arguments there are
     */
    public int length() {
        return getArgs().size();
    }
    
    /**
     * Checks if the length of the command matches a specific length
     *
     * @param length The length to match
     * @return True if the length of the command matches the length parameter
     */
    public boolean isLength(int length) {
        return length() == length;
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
     * Gets the plugin
     *
     * @return The plugin
     */
    public ICoPlugin getPlugin() {
        return plugin;
    }
    
    /**
     * Gets the command sender
     *
     * @return The command sender
     */
    public CoSender getSender() {
        return sender;
    }
    
    /**
     * Gets a list of arguments from the command
     *
     * @return The command arguments.
     */
    public List<String> getArgs() {
        return args;
    }
    
    /**
     * Checks if the command ran had arguments
     *
     * @return True if it had arguments
     */
    public boolean hasArgs() {
        return !args.isEmpty();
    }
    
    /**
     * Gets an argument at a specified index
     *
     * @param index The index to get the arg at.
     * @return The arg at the specified index. If none exists its null.
     */
    public String argAt(int index) {
        if (index < 0 || index >= args.size()) {
            return null;
        }
        return args.get(index);
    }
    
    /**
     * Joins arguments from one index of the command to another.
     *
     * @param start  The start index
     * @param finish The end index
     * @return One string combining the strings between the two index's
     */
    public String joinArgs(int start, int finish) {
        if (args.isEmpty()) {
            return "";
        }
        return String.join(" ", args.subList(start, finish));
    }
    
    /**
     * Joins all the arguments after a specific index.
     *
     * @param start Where to join the strings at
     * @return One string combining all the strings after the index
     */
    public String joinArgs(int start) {
        return joinArgs(start, args.size());
    }
    
    /**
     * Joins all the command arguments together
     *
     * @return All the command arguments in one string
     */
    public String joinArgs() {
        return joinArgs(0);
    }
    
    /**
     * Sends a message to the player/console.
     *
     * @param message The message to send.
     */
    public void send(String message) {
        sender.sendMessage(message);
    }
    
    /**
     * Sends a message to the player/console
     * @param text The text object to send
     * @param objects The placeholders to use
     */
    public void send(Text.TextSection text, Object... objects) {
        final CoFormatter formatter = plugin.getCoFormatter();
        if (getSender().getType().equals(SenderType.PLAYER)) Coalesce.sendRawMessage(getSender().as(Player.class), formatter.formatString(text.toString(), objects));
        else send(formatter.format(text.toUnformatted(), objects));
    }
    
    /**
     * Sends a text object to the sender
     * @param text The text to send
     */
    public void send(Text.TextSection text) {
        if (getSender().getType().equals(SenderType.PLAYER)) {
            Coalesce.sendMessage(getSender().as(Player.class), text);
        }
        else send(text.toUnformatted());
    }
    
    /**
     * Sends a formatted plguin message.
     * <p>
     * <p>Ex. [PlguinName] MESSAGE FROM THE PARAMETER</p>
     *
     * @param message The message to send with the plugin name before it.
     */
    public void pluginMessage(String message) {
        send(plugin.getCoFormatter().format(message));
    }
    
    /**
     * Sends a formatted plugin message.
     *
     * @param message The message to format
     * @param objects The objects used to replace the placeholders.
     */
    public void pluginMessage(String message, Object... objects) {
        send(plugin.getCoFormatter().format(message, objects));
    }
    
    /**
     * Sends a text message to the sender.
     * @param text The text to send
     */
    public void pluginMessage(Text.TextSection text) {
        Text.TextSection pluginMessage = Text.of("[").setColor(Color.GRAY);
        pluginMessage.append(Color.stripColor(plugin.getDisplayName())).setColor(plugin.getPluginColor());
        pluginMessage.append("] ").setColor(Color.GRAY);
        send(pluginMessage);
    }
    
    /**
     * Sends a formatted message to the sender
     * @param text The text object to use
     * @param objects The object to replace the placeholders with
     */
    public void pluginMessage(Text.TextSection text, Object... objects) {
        final CoFormatter formatter = plugin.getCoFormatter();
        Text.TextSection pluginMessage = Text.of("[").setColor(Color.GRAY);
        pluginMessage.append(Color.stripColor(plugin.getDisplayName())).setColor(plugin.getPluginColor());
        pluginMessage.append("] ").setColor(Color.GRAY);
        text.getSections().forEach(pluginMessage::append);
        if (getSender().getType().equals(SenderType.PLAYER)) Coalesce.sendRawMessage(getSender().as(Player.class), formatter.formatString(pluginMessage.toString(), objects));
        else send(formatter.format(pluginMessage.toUnformatted(), objects));
    }
    
    /**
     * Sends the sender the default, formatted, noPermissions message
     *
     * @param permissionsNeeded The permissions required to run this command
     */
    public void noPermission(String... permissionsNeeded) {
        send(plugin.getCoFormatter().format(Color.RED + "You do not have sufficient permission to run " + "this command! Permission(s) required: " + Color.SILVER + Arrays.toString(permissionsNeeded)));
    }
    
    /**
     * Sends the sender the default, formatted, tooManyArgs message
     *
     * @param max   The maximum arguments allowed in the command
     * @param given The amount given
     */
    public void tooManyArgs(int max, int given) {
        send(plugin.getCoFormatter().format(Color.RED + "Too many arguments supplied to run command! " + Color.RED + "Maximum: " + Color.SILVER + max + Color.RED + " Given: " + Color.SILVER + given));
    }
    
    /**
     * Sends the sender the default, formatted, notEnoughArgs message
     *
     * @param min   The minimum arguments allowed in the command
     * @param given The amount given
     */
    public void notEnoughArgs(int min, int given) {
        send(plugin.getCoFormatter().format(Color.RED + "Not enough arguments supplied to run command! " + Color.RED + "Minimum: " + Color.SILVER + min + Color.RED + " Given: " + Color.SILVER + given));
    }
    
    /**
     * Sends the sender the default, formatted, notCorrectSender message
     * @param allowed The sender type that is allowed.
     */
    public void notCorrectSender(SenderType... allowed) {
        send(plugin.getCoFormatter().format(Color.RED + "You are not the correct sender type to run this command! You must be one of the following: " + Color.SILVER + Arrays.toString(Stream.of(allowed).map(t -> t.toString().toLowerCase() + ",").toArray())));
    }
    
    /**
     * Sends the sender a regular message with placeholder parsing
     *
     * @param message The message to send
     * @param objects The placeholders to use
     */
    public void send(String message, Object... objects) {
        send(plugin.getCoFormatter().formatString(message, objects));
    }
    
    /**
     * Runs a sub command of this command if the sender matches the given sender type
     *
     * @param senderType The type of sender needed to run the subCommand
     * @param executor   The command method (method reference)
     * @return true if the senderType passed. False otherwise
     */
    public boolean subCommand(SenderType senderType, CommandExecutor<C> executor) {
        if (getSender().getType() == senderType) {
            executor.run((C)this);
            return true;
        }
        return false;
    }
    
    /**
     * Runs a sub command of this command if the index of the command matches the given index
     *
     * @param index    The index needed to run the command
     * @param executor The command method (method reference)
     * @return true if the index passed. False otherwise.
     */
    public boolean subCommandAt(int index, CommandExecutor<C> executor) {
        if (getArgs().size() - 1 == index) {
            executor.run((C)this);
            return true;
        }
        return false;
    }
    
    /**
     * Runs a sub command of this command if the index of the command matches the given argument
     *
     * @param index index to look for the specified argument
     * @param match The argument needed to be matched
     * @param ignoreCase Ignores the case of the given argument
     * @param executor The command method (method reference)
     * @return true if the arg at the specified index matches the given string
     */
    public boolean subCommandAt(int index, String match, boolean ignoreCase, CommandExecutor<C> executor) {
        if ((ignoreCase ? argAt(index).equalsIgnoreCase(match) : argAt(index).equals(match))) {
            executor.run((C)this);
            return true;
        }
        return false;
    }
    
    /**
     * Runs a sub command of this command if the index matches the given index and if the sender matches the given sender type
     *
     * @param index      The index needed to run the command
     * @param senderType The type of sender needed to run the subCommand
     * @param executor   The command method (method reference)
     * @return true if the index and the senderType passed. False otherwise
     */
    public boolean subCommandAt(int index, SenderType senderType, CommandExecutor<C> executor) {
        if (getArgs().size() - 1 == index && getSender().getType() == senderType) {
            executor.run((C)this);
            return true;
        }
        return false;
    }
    
    /**
     * Runs a sub command of this command if the predicate passes.
     *
     * @param predicate The predicate needed to pass
     * @param executor  The command method (method reference)
     * @return true if the predicate passed. False otherwise
     */
    public boolean subCommand(Predicate<CommandContext<C, T>> predicate, CommandExecutor<C> executor) {
        if (predicate.test(this)) {
            executor.run((C)this);
            return true;
        }
        return false;
    }
}
