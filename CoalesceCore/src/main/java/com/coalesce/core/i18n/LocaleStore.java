package com.coalesce.core.i18n;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public final class LocaleStore {
    
    private final  Map<Locale, CoLang<? extends Enum>> language;
    
    public LocaleStore() {
        this.language = new HashMap<>();
    }
    
    @SuppressWarnings("unchecked")
    public <T extends Enum> CoLang<T> getCoLang(Class<T> type, Locale locale) {
        return (CoLang<T>)language.get(locale);
    }
    
}
