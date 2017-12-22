package com.coalesce.core.config.base;

import com.coalesce.core.config.common.Section;
import com.coalesce.core.plugin.ICoPlugin;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public interface ISection {
    
    /**
     * Gets a set of keys that are in this section.
     *
     * @param deep Whether to get the keys of the keys (and so on) in this section.
     * @return A Set of Keys.
     */
    default Set<String> getKeys(boolean deep) {
        Set<String> keys = new HashSet<>();
        if (deep) {
            getConfig().getEntries().stream().filter(e -> e.getPath().startsWith(getCurrentPath() + ".")).forEach(e -> keys.add(e.getPath()));
        } else {
            getConfig().getEntries().stream().filter(e -> e.getPath().startsWith(getCurrentPath() + ".")).forEach(e -> {
                String key = e.getPath().substring(getCurrentPath().length() + 1);
                int size = key.indexOf(".");
                if (size < 0) {
                    size = key.length();
                }
                keys.add(key.substring(0, size));
            });
        }
        return keys;
    }
    
    /**
     * Gets all the entries that exist in this configuration section.
     *
     * @return A set of entries in this section.
     */
    default Set<IEntry> getEntries() {
        return getConfig().getEntries().stream().filter(e -> e.getPath().startsWith(getCurrentPath() + ".")).collect(Collectors.toSet());
    }
    
    /**
     * Gets a section from within this section.
     *
     * @param path The path from the start of this section. <p>Note: dont use the string provided originally to get this current section</p>
     * @return A section of the configuration.
     */
    default ISection getSection(String path) {
        return new Section(getCurrentPath() + "." + path, getConfig(), getPlugin());
    }
    
    /**
     * Check whether a section contains a path.
     *
     * @param path The path to look for
     * @return True if the path exists, false otherwise.
     */
    default boolean contains(String path) {
        return getKeys(false).contains(path);
    }
    
    /**
     * Gets the current path of the configuration section.
     *
     * @return The current path
     */
    String getCurrentPath();
    
    /**
     * The name of the section.
     *
     * @return The name of the section.
     */
    default String getName() {
        return getCurrentPath().substring(getCurrentPath().lastIndexOf("."));
    }
    
    /**
     * Gets the base configuration of this section.
     *
     * @return The current configuration
     */
    IConfig getConfig();
    
    /**
     * Gets the host plugin.
     *
     * @return The host plugin.
     */
    ICoPlugin getPlugin();
    
    /**
     * Gets an entry from a section
     *
     * @param path The path to the entry. <p>Note: dont use the string provided originally to get this entry</p> //TODO: elaborate more and create getEntry Implementations in Json and yml config.
     */
    default IEntry getEntry(String path) {
        return getConfig().getEntry(getCurrentPath() + "." + path);
    }
    
    /**
     * Gets the parent section to the current section.
     *
     * @return THe previous section, returns null if no previous section exists.
     */
    default ISection getParent() {
        if (!getCurrentPath().contains(".")) {
            return null;
        } else {
            return new Section(getCurrentPath().substring(0, getCurrentPath().lastIndexOf(".")), getConfig(), getPlugin());
        }
    }
    
}
