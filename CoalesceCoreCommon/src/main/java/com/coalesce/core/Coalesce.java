package com.coalesce.core;

import com.coalesce.core.plugin.ICoPlugin;
import com.coalesce.core.wrappers.CoPlayer;

import java.util.Collection;

/**
 * Static access to the CoalesceCore API
 */
public final class Coalesce {
    
    private static ICoPlugin coalesce;
    
    private Coalesce() {}
    
    public static void setCoalesce(ICoPlugin coalesce) {
        if (Coalesce.coalesce != null) {
            throw new UnsupportedOperationException("Can't define a new Coalesce when its already defined.");
        }
        else Coalesce.coalesce = coalesce;
    }
    
    public static <T> Collection<CoPlayer<T>> getCoPlayers() {
        return coalesce.getCoPlayers();
    }
    
    public static <T> CoPlayer<T> getCoPlayer(String name) {
        return coalesce.getCoPlayer(name);
    }
    
}
