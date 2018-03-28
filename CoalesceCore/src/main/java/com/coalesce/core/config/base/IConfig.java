package com.coalesce.core.config.base;

import com.coalesce.core.plugin.ICoPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

@SuppressWarnings({"unused"})
public interface IConfig extends ISection {
    
    @Override
    String getName();
    
    @Override
    Set<String> getKeys(boolean deep);
    
    @Override
    Object getValue(String path);
    
    @Override
    ICoPlugin getPlugin();
    
    @Override
    default String getCurrentPath() {
        return "";
    }
    
    @Override
    void addEntry(String path, Object value);
    
    @Override
    void setEntry(String path, Object value);
    
    /**
     * Gets the config file.
     *
     * @return The file of this config.
     */
    File getFile();
    
    /**
     * Returns this config.
     *
     * @return This current config.
     */
    default IConfig getConfig() {
        return this;
    }
    
    /**
     * Removes an entry from the config via the entry path.
     *
     * @param path The path to this entry.
     */
    default void removeEntry(String path) {
        setEntry(path, null);
    }
    
    /**
     * Clears all the entries in this configuration.
     */
    default void clear() {
        getKeys(true).forEach(this::removeEntry);
    }
    
    /**
     * Backs up this configuration.
     */
    @SuppressWarnings( "ResultOfMethodCallIgnored" )
    default boolean backup() {
        DateFormat format = new SimpleDateFormat("yyyy.dd.MM-hh.mm.ss");
        File file = new File(getDirectory() + File.separator + "backups");
        File bckp = new File(file + File.separator + getName() + format.format(new Date()) + ".yml");
        return backup(file, bckp);
    }
    
    @SuppressWarnings( "ResultOfMethodCallIgnored" )
    default boolean backup(File location, File newFile) {
    
        try {
            if (!location.exists()) location.mkdirs();
            if (newFile.exists()) return false;
            newFile.createNewFile();
            Files.copy(getFile().toPath(), newFile.toPath());
            return true;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Backs up the configuration
     *
     * @param file The file to back the configuration up to.
     */
    default boolean backup(File file) {
        return backup(new File(getDirectory() + File.separator + "backups"), file);
    }
    
    /**
     * Creates the new file if it doesn't exist.
     *
     * @return true if the file was created, false otherwise.
     */
    @SuppressWarnings("all")
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
