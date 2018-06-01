package com.coalesce.core;

import com.coalesce.core.plugin.ICoModule;
import com.coalesce.core.plugin.ICoPlugin;
import org.bukkit.event.Listener;

public abstract class CoModule implements Listener, ICoModule {

    private final ICoPlugin plugin;
    private final String name;
    private boolean isEnabled;

    /**
     * Creates a new module.
     */
    public CoModule(ICoPlugin plugin, String name) {
        this.plugin = plugin;
        this.name = name;
    }
    
    @Override
    public boolean isEnabled() {
        return isEnabled;
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public ICoPlugin getPlugin() {
        return plugin;
    }

    @Override
    public void enable() {
        if (isEnabled) {
            throw new IllegalStateException("Module " + getName() + " is already disabled.");
        }

        try {
            onEnable();
            isEnabled = true;
            onPostEnable();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void disable() {
        if (!isEnabled) {
            throw new IllegalStateException("Module " + getName() + " isn't enabled.");
        }

        try {
            onDisable();
            isEnabled = false;
            onPostDisable();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPostDisable() throws Exception {
    }

    @Override
    public void onPostEnable() throws Exception {
    }

    @Override
    public abstract void onEnable() throws Exception;

    @Override
    public abstract void onDisable() throws Exception;

}
