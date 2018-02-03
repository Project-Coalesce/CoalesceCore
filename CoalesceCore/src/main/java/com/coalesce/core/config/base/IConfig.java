package com.coalesce.core.config.base;

import com.coalesce.core.config.common.Section;
import com.coalesce.core.plugin.ICoPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@SuppressWarnings({"unused"})
public interface IConfig {
    
    /**
     * Gets the owning plugin
     *
     * @return The owning plugin
     */
    ICoPlugin getPlugin();
    
    /**
     * Gets all the keys (as strings) in this configuration.
     *
     * @return The set of keys.
     */
    Set<String> getKeys(boolean deep);
    
    /**
     * Gets all the entries in a config.
     *
     * @return A collection of entries in a config.
     */
    Collection<IEntry> getEntries();
    
    /**
     * Adds a new entry to the current config.
     *
     * @param path  The path in the config.
     * @param value The value to set this entry to.
     */
    void addEntry(String path, Object value);
    
    /**
     * Adds a new entry to the current config. If the
     * config already has a value at the path location
     * it will be updated with the new value supplied
     * from this method.
     *
     * @param path  The path in the config.
     * @param value The value to set the path to.
     */
    void setEntry(String path, Object value);
    
    /**
     * The name of the config
     *
     * @return The name of the current config.
     */
    String getName();
    
    /**
     * Gets the config file.
     *
     * @return The file of this config.
     */
    File getFile();
    
    /**
     * Gets a value from a config entry.
     *
     * @param path The path in the configuration.
     * @return An object from the specified path.
     */
    default Object getValue(String path) {
        if (contains(path, true)) {
            return getEntry(path).getValue();
        } else {
            return null;
        }
    }
    
    /**
     * Checks if this configuration contains a specified path
     *
     * @param path  The path to look for
     * @param exact Whether the path to look for needs to be an entry or if it just needs to exist.
     * @return True if the path exists.
     */
    default boolean contains(String path, boolean exact) {
        if (exact) {
            return getEntry(path) != null;
        } else {
            for (IEntry entry : getEntries()) {
                if (entry.getPath().startsWith(path)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * Gets a section of the configuration
     *
     * @param name The path of this section
     * @return The section
     */
    default ISection getSection(String name) {
        return new Section(name, this, getPlugin());
    }
    
    /**
     * Gets an entry from a config.
     *
     * @param path The path in the config.
     * @return The entry that is at that path.
     */
    default IEntry getEntry(String path) {
        for (IEntry entry : getEntries()) {
            if (entry.getPath().matches(path)) {
                return entry;
            }
        }
        return null;
    }
    
    /**
     * Returns this config.
     *
     * @return This current config.
     */
    default IConfig getConfig() {
        return this;
    }
    
    /**
     * Gets a value from a config entry.
     *
     * @param path The path in the configuration.
     * @return A string from the specified path.
     */
    default String getString(String path) {
        return getEntry(path).getAs(String.class);
    }
    
    /**
     * Gets a value from a config entry.
     *
     * @param path The path in the configuration.
     * @return A double from the specified path.
     */
    default double getDouble(String path) {
        return getEntry(path).getAs(Double.class);
    }
    
    /**
     * Gets a value from a config entry.
     *
     * @param path The path in the configuration.
     * @return An integer from the specified path.
     */
    default int getInt(String path) {
        return getEntry(path).getAs(Integer.class);
    }
    
    /**
     * Gets a value from a config entry.
     *
     * @param path The path in the configuration.
     * @return A long from the specified path.
     */
    default long getLong(String path) {
        return getEntry(path).getAs(Long.class);
    }
    
    /**
     * Gets a value from a config entry.
     *
     * @param path The path in the configuration.
     * @return A boolean from the specified path.
     */
    default boolean getBoolean(String path) {
        return getEntry(path).getAs(Boolean.class);
    }
    
    /**
     * Gets a value from a config entry.
     *
     * @param path The path in the configuration.
     * @return A list from the specified path.
     */
    default List<?> getList(String path) {
        return getEntry(path).getAs(List.class);
    }
    
    /**
     * Gets a value from a config entry.
     *
     * @param path The path in the configuration.
     * @return A flat from the specified path.
     */
    default float getFloat(String path) {
        return getEntry(path).getAs(Float.class);
    }
    
    /**
     * Gets a specific type of list.
     *
     * @param path The path to the list
     * @param type The type of list to return
     * @param <E>  The list type
     * @return The list
     */
    @SuppressWarnings( {"unchecked", "unused"} )
    default <E> List<E> getList(String path, Class<E> type) {
        return (List<E>)getList(path);
    }
    
    /**
     * Gets a List of Strings from a path in this config.
     *
     * @param path The path to get the strings from.
     * @return A list from the specified path.
     */
    default List<String> getStringList(String path) {
        return getList(path, String.class);
    }
    
    /**
     * Gets an entry based on its value instead of path.
     *
     * @param value The value you are looking for.
     * @return The entry that was found with this value.
     */
    default Collection<IEntry> getEntryFromValue(Object value) {
        return getEntries().stream().filter(e -> e.getValue().equals(value)).collect(Collectors.toSet());
    }
    
    /**
     * Removes an entry from the config via the Entry Object.
     *
     * @param entry The entry to remove.
     */
    default void removeEntry(IEntry entry) {
        entry.remove();
    }
    
    /**
     * Removes an entry from the config via the entry path.
     *
     * @param path The path to this entry.
     */
    default void removeEntry(String path) {
        getEntry(path).remove();
    }
    
    /**
     * Clears all the entries in this configuration.
     */
    default void clear() {
        getEntries().forEach(IEntry::remove);
    }
    
    /**
     * Backs up this configuration.
     */
    @SuppressWarnings( "ResultOfMethodCallIgnored" )
    default void backup() {
        DateFormat format = new SimpleDateFormat("yyyy.dd.MM-hh.mm.ss");
        File file = new File(getDirectory() + File.separator + "backups");
        File bckp = new File(file + File.separator + getName() + format.format(new Date()) + ".yml");
        if (!file.exists()) {
            file.mkdir();
        }
        backup(bckp);
    }
    
    /**
     * Backs up the configuration
     *
     * @param file The file to back the configuration up to.
     */
    default void backup(File file) {
        if (!getDirectory().exists() || file.exists() || !getFile().exists()) {
            return;
        }
        try {
            Files.copy(getFile().toPath(), file.toPath());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Creates the new file if it doesn't exist.
     *
     * @return true if the file was created, false otherwise.
     */
    default boolean create() {
        try {
            return getFile().createNewFile();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Deletes the configuration file.
     */
    default boolean delete() {
        return getFile().delete();
    }
    
    /**
     * Gets the directory this config file is held in
     *
     * @return The directory.
     */
    default File getDirectory() {
        return getFile().getParentFile();
    }
    
}
