package com.coalesce.core.wrappers;

import com.coalesce.core.SenderType;
import com.coalesce.core.plugin.ICoPlugin;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class CoSender {
    
    private final ICoPlugin plugin;
    private final CommandSender sender;
    
    public CoSender(ICoPlugin plugin, CommandSender sender) {
        this.sender = sender;
        this.plugin = plugin;
    }
    
    /**
     * Sends the sender a message
     *
     * @param message The message to send
     */
    public void sendMessage(String message) {
        sender.sendMessage(message);
    }
    
    /**
     * Sends a series of messages to the sender
     *
     * @param messages The messages to send
     */
    public void sendMessage(String... messages) {
        for (String message : messages) {
            sendMessage(message);
        }
    }
    
    /**
     * Sends a formatted plugin message.
     *
     * @param message The message to send
     */
    public void pluginMessage(String message) {
        sendMessage(getPlugin().getCoFormatter().format(message));
    }
    
    /**
     * Sends a series of plugin messages to the sender
     *
     * @param messages The messages to send
     */
    public void pluginMessage(String... messages) {
        for (String message : messages) {
            pluginMessage(message);
        }
    }
    
    /**
     * Checks if the sender has any of the permissions specified
     *
     * @param permissions The permissions to look for.
     */
    public boolean hasAnyPermission(String... permissions) {
        for (String permission : permissions) {
            if (hasPermission(permission)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Checks to see if the sender has all of the permissions specified
     *
     * @param permissions The permissions to look for.
     */
    public boolean hasAllPermissions(String... permissions) {
        for (String permission : permissions) {
            if (!hasPermission(permission)) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * The name of the sender
     *
     * @return The name of the sender
     */
    public String getName() {
        return sender.getName();
    }
    
    /**
     * Gets the type of sender being used
     *
     * @return The {@link SenderType}
     */
    public SenderType getType() {
        if (sender instanceof ConsoleCommandSender) {
            return SenderType.CONSOLE;
        }
        if (sender instanceof BlockCommandSender) {
            return SenderType.BLOCK;
        }
        if (sender instanceof Player) {
            return SenderType.PLAYER;
        } else {
            return SenderType.OTHER;
        }
    }
    
    /**
     * Checks if the sender has permission.
     *
     * @param permission The permission to look for
     * @return True if the sender has permission.
     */
    public boolean hasPermission(String permission) {
        return sender.hasPermission(permission);
    }
    
    /**
     * The base player of the platform currently being used
     *
     * @return The platforms player class
     */
    public CommandSender getBase() {
        return sender;
    }
    
    /**
     * Returns the sender as a specific type.
     *
     * @param type Type must extend the type of sender
     * @return The base sender as a specific type.
     * <p>
     * <p>
     * E.g. CoSender#as(Player.class); returns the sender as a player.
     * </p>
     */
    public <E extends CommandSender> E as(Class<E> type) {
        return (E)sender;
    }
    
    /**
     * The owning plugin
     *
     * @return The plugin
     */
    public ICoPlugin getPlugin() {
        return plugin;
    }
    
}
