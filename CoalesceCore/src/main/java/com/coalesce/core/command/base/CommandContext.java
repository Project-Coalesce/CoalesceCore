package com.coalesce.core.command.base;

import com.coalesce.core.Coalesce;
import com.coalesce.core.Color;
import com.coalesce.core.SenderType;
import com.coalesce.core.chat.CoFormatter;
import com.coalesce.core.command.builder.interfaces.CommandExecutor;
import com.coalesce.core.i18n.Translatable;
import com.coalesce.core.plugin.ICoPlugin;
import com.coalesce.core.text.Text;
import com.coalesce.core.wrappers.CoSender;
import org.bukkit.Location;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;
import java.util.stream.Stream;

@SuppressWarnings({"unused", "unchecked", "WeakerAccess"})
public abstract class CommandContext<C extends CommandContext<C, T, M, B, P>, T extends TabContext<C, T, M, B, P>, M extends Enum & Translatable, B extends CommandBuilder<C, T, M, B, P>, P extends ProcessedCommand<C, T, M, B, P>> {
    
    private ICoPlugin<M> plugin;
    private final String alias;
    private final CoSender sender;
    private final List<String> args;
    
    public CommandContext(CoSender sender, String alias, String[] args, ICoPlugin<M> plugin) {
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
    public ICoPlugin<M> getPlugin() {
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
     * Checks if the sender is the console.
     * @return True if the sender is the console, false otherwise.
     */
    public boolean isConsole() {
        return getSender().getType() == SenderType.CONSOLE;
    }
    
    /**
     * Checks if the sender is a block
     * @return True if the sender is a block sender, false otherwise.
     */
    public boolean isBlock() {
        return getSender().getType() == SenderType.BLOCK;
    }
    
    /**
     * Checks if the sender is a player.
     * @return True if the sender is a player, false otherwise.
     */
    public boolean isPlayer() {
        return getSender().getType() == SenderType.PLAYER;
    }
    
    /**
     * Checks if the sender is an entity.
     * @return True if the sender is an entity, false otherwise.
     */
    public boolean isEntity() {
        return getSender().getType() == SenderType.ENTITY;
    }
    
    /**
     * Checks if the sender is locatable.
     * @return True if the sender is a block, a player, or an entity. False otherwise.
     */
    public boolean isLocatable() {
        return isBlock() || isPlayer() || isEntity();
    }
    
    /**
     * Gets the sender as a player
     * @return The player if the sender is a player, null otherwise.
     */
    public Player asPlayer() {
        return isPlayer() ? as(Player.class) : null;
    }
    
    /**
     * Gets the sender as an entity
     * @return The entity if the sender is an entity, null otherwise.
     */
    public Entity asEntity() {
        return isEntity() ? as(Entity.class) : null;
    }
    
    /**
     * Gets the sender as a block sender
     * @return The block sender if the sender is a block sender, null otherwise.
     */
    public BlockCommandSender asBlock() {
        return isBlock() ? as(BlockCommandSender.class) : null;
    }
    
    /**
     * Gets the sender as the desired type
     * @param type The type class.
     * @param <S> The class specified must extend CommandSender
     * @return The sender as the desired type.
     */
    public <S extends CommandSender> S as(Class<S> type) {
        return getSender().as(type);
    }
    
    /**
     * Gets the location of the sender.
     * @return The location of the sender if it is locatable. Null otherwise.
     */
    public Location getLocation() {
        return isLocatable() ? getSender().getLocation() : null;
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
     * @param start  The start index (inclusive)
     * @param finish The end index (exclusive)
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
     * @param start Where to join the strings at (inclusive)
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
     * Sends the sender a regular message with placeholder parsing
     *
     * @param message The message to send
     * @param objects The placeholders to use
     */
    public void send(String message, Object... objects) {
        send(plugin.getCoFormatter().formatString(message, objects));
    }
    
    /**
     * Sends a translated message to the sender in the current language. (Only if supported by the plugin)
     * @param messageKey The key needing to be sent
     */
    public void send(M messageKey) {
        send(messageKey, plugin.getLocaleStore().getDefaultLocale());
    }
    
    /**
     * Sends a translated message to the sender in the desired language. (Only if supported by the plugin)
     * @param messageKey The key needing to be sent
     * @param locale The desired locale.
     */
    public void send(M messageKey, Locale locale) {
        send(plugin.getLocaleStore().translate(messageKey, locale));
    }
    
    /**
     * Sends a translated message to the sender in the current language. (Only if supported by the plugin)
     * @param messageKey The key needing to be sent
     * @param objects The placeholder objects to use
     */
    public void send(M messageKey, Object... objects) {
        send(messageKey, plugin.getLocaleStore().getDefaultLocale(), objects);
    }
    
    /**
     * Sends a translated message to the sender in the desired language. (Only if supported by the plugin)
     * @param messageKey The key needing to be sent
     * @param locale The desired locale.
     * @param objects The placeholder objects to use
     */
    public void send(M messageKey, Locale locale, Object... objects) {
        send(plugin.getLocaleStore().translate(messageKey, locale, objects));
    }
    
    /**
     * Sends a text object to the sender
     * @param text The text to send
     */
    public void send(Text.TextSection text) {
        if (sender.getType().equals(SenderType.PLAYER)) {
            Coalesce.sendMessage(sender.as(Player.class), text);
        }
        else send(text.toUnformatted());
    }
    
    /**
     * Sends a message to the player/console
     * @param text The text object to send
     * @param objects The placeholders to use
     */
    public void send(Text.TextSection text, Object... objects) {
        final CoFormatter formatter = plugin.getCoFormatter();
        if (sender.getType().equals(SenderType.PLAYER)) Coalesce.sendRawMessage(getSender().as(Player.class), formatter.formatString(text.toString(), objects));
        else send(formatter.format(text.toUnformatted(), objects));
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
     * Sends a formatted plugin message in the current language. (Only if supported by the plugin)
     * @param messageKey The message key to send
     */
    public void pluginMessage(M messageKey) {
        pluginMessage(messageKey, plugin.getLocaleStore().getDefaultLocale());
    }
    
    /**
     * Sends a formatted plugin message in the desired language. (Only if supported by the plugin)
     * @param messageKey The message key to send
     * @param locale The desired locale
     */
    public void pluginMessage(M messageKey, Locale locale) {
        send(plugin.getCoFormatter().format(plugin.getLocaleStore().translate(messageKey, locale)));
    }
    
    /**
     * Sends a formatted plugin message in the current language. (Only if supported by the plugin)
     * @param messageKey The message key to send
     * @param objects The placeholder objects to use
     */
    public void pluginMessage(M messageKey, Object... objects) {
        pluginMessage(messageKey, plugin.getLocaleStore().getDefaultLocale(), objects);
    }
    
    /**
     * Sends a formatted plugin message in the desired language. (Only if supported by the plugin)
     * @param messageKey The message key to send
     * @param locale The desired locale
     * @param objects The placeholder objects to use
     */
    public void pluginMessage(M messageKey, Locale locale, Object... objects) {
        send(plugin.getCoFormatter().format(plugin.getLocaleStore().translate(messageKey, locale, objects)));
    }
    
    /**
     * Sends a text message to the sender.
     * @param text The text to send
     */
    public void pluginMessage(Text.TextSection text) {
        Text.TextSection pluginMessage = Text.of("[").setColor(Color.GRAY);
        pluginMessage.append(Color.stripColor(plugin.getDisplayName())).setColor(plugin.getPluginColor());
        pluginMessage.append("] ").setColor(Color.GRAY);
        text.getSections().forEach(pluginMessage::append);
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
     * Runs a sub command of this command if the sender matches the given sender type
     *
     * @param senderType The type of sender needed to run the subCommand
     * @param executor   The command method (method reference)
     * @return true if the senderType passed. False otherwise
     */
    public boolean subCommand(SenderType senderType, CommandExecutor<C, T, M, B, P> executor) {
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
    public boolean subCommandAt(int index, CommandExecutor<C, T, M, B, P> executor) {
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
    public boolean subCommandAt(int index, String match, boolean ignoreCase, CommandExecutor<C, T, M, B, P> executor) {
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
    public boolean subCommandAt(int index, SenderType senderType, CommandExecutor<C, T, M, B, P> executor) {
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
    public boolean subCommand(Predicate<CommandContext<C, T, M, B, P>> predicate, CommandExecutor<C, T, M, B, P> executor) {
        if (predicate.test(this)) {
            executor.run((C)this);
            return true;
        }
        return false;
    }
}
