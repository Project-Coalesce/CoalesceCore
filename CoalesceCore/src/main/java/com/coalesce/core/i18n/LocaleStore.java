package com.coalesce.core.i18n;

import com.coalesce.core.plugin.ICoPlugin;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public final class LocaleStore {
    
    private final Map<ICoPlugin, Map<Locale, Map<? extends Enum, String>>> language;
    
    public LocaleStore() {
        this.language = new HashMap<>();
    }
    
}
