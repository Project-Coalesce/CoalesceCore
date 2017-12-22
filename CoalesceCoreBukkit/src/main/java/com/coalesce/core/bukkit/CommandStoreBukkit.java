package com.coalesce.core.bukkit;

import com.coalesce.core.Color;
import com.coalesce.core.command.base.CommandInfo;
import com.coalesce.core.command.base.CommandStore;
import com.coalesce.core.plugin.ICoPlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;

import java.lang.reflect.Field;

public final class CommandStoreBukkit extends CommandStore {

    private final ICoPlugin plugin;
    private CommandMap bukkitCommandMap;

    public CommandStoreBukkit(ICoPlugin plugin) {
        super(plugin);
        this.plugin = plugin;
        try {

            Field field = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            field.setAccessible(true);
            this.bukkitCommandMap = (CommandMap)field.get(Bukkit.getServer());
        }

        catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void registerCommand(CommandInfo info) {
        if (this.bukkitCommandMap == null) {
            throw new RuntimeException("Bukkit CommandMap could not be found");
        }
        if (!isRegistered(info.getName())) {
            bukkitCommandMap.register(Color.stripColor(plugin.getDisplayName()), new CommandRegister(info, plugin));
        }
    }

    @Override
    public boolean isRegistered(String commandName) {
        if (this.bukkitCommandMap == null) {
            throw new RuntimeException("Bukkit CommandMap could not be found");
        }
        return this.bukkitCommandMap.getCommand(commandName) != null;
    }

    @Override
    protected void registerObjects() {
        register();
    }
}
