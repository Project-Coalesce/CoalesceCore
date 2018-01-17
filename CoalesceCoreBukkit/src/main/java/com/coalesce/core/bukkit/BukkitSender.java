package com.coalesce.core.bukkit;

import com.coalesce.core.wrappers.CoSender;
import com.coalesce.core.SenderType;
import com.coalesce.core.plugin.ICoPlugin;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class BukkitSender implements CoSender<CommandSender> {
    
    private final ICoPlugin plugin;
    private final CommandSender sender;
    
    public <T extends CommandSender> BukkitSender(T sender, ICoPlugin plugin) {
        this.sender = sender;
        this.plugin = plugin;
    }
    
    @Override
    public void sendMessage(String message) {
        sender.sendMessage(message);
    }
    
    @Override
    public String getName() {
        return sender.getName();
    }
    
    @Override
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
    
    @Override
    public boolean hasPermission(String permission) {
        return sender.hasPermission(permission);
    }
    
    @Override
    public CommandSender getBase() {
        return sender;
    }
    
    @Override
    public <E extends CommandSender> E as(Class<E> type) {
        return (E)sender;
    }
    
    @Override
    public ICoPlugin getPlugin() {
        return plugin;
    }
    
    
}
