package com.coalesce.core.plugin;

import java.util.concurrent.Executors;

public interface ICoModule {
    
    /**
     * Checks if this module is enabled.
     *
     * @return True if enabled
     */
    boolean isEnabled();
    
    /**
     * Gets the name of this module.
     *
     * @return The name of this module.
     */
    String getName();
    
    /**
     * Gets the plugin this module belongs to.
     *
     * @return The owning plugin.
     */
    ICoPlugin getPlugin();
    
    /**
     * Enables the module
     */
    void enable();
    
    /**
     * Disables the module
     */
    void disable();
    
    /**
     * Executed after the onEnable method.
     * <p>The onEnable method will show the module as not being enabled when {@link #isEnabled()} is called. At this stage, the {@link #isEnabled()} method will be true</p>
     */
    void onPostEnable() throws Exception;
    
    /**
     * Executed after the onDisable method.
     * <p>The onDisable method will show the module as still being enabled when {@link #isEnabled()} is called. At this stage, the {@link #isEnabled()} method will return false.</p>
     */
    void onPostDisable() throws Exception;
    
    /**
     * Method called when a module is enabled
     */
    void onEnable() throws Exception;
    
    /**
     * Method called when a module is disabled
     */
    void onDisable() throws Exception;
    
}
