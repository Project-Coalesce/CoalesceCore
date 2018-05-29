package com.coalesce.core.i18n;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public final class LocaleStore<M extends Enum & Translatable> {
    
    private final Map<Locale, CoLang<M>> localeMap;
    
    private Locale locale;
    
    public LocaleStore() {
        this(Locale.ENGLISH);
    }
    
    public LocaleStore(Locale locale) {
        this.localeMap = new HashMap<>();
        this.locale = locale;
    }
    
    /**
     * Gets a CoLang from the corresponding locale.
     * @param locale The locale to get the CoLang from
     * @return The CoLang if it is loaded and exists. Null otherwise.
     */
    public CoLang<M> getCoLang(Locale locale) {
        return localeMap.get(locale);
    }
    
    /**
     * Loads a CoLang into the LocaleStore
     * @param coLang The CoLang to load.
     */
    public void loadCoLang(CoLang<M> coLang) {
        localeMap.put(coLang.getLocale(), coLang);
    }
    
    /**
     * Unloads a language from this plugins LocaleStore
     * @param locale The locale to remove.
     * @return True if it was successfully removed, false otherwise.
     */
    public boolean unLoadCoLang(Locale locale) {
        return localeMap.remove(locale) != null;
    }
    
    public Locale getDefaultLocale() {
        return locale;
    }
    
    public void setDefaultLocale(Locale locale) {
        this.locale = locale;
    }
    
    public String translate(M key) {
        return getCoLang(locale).translate(key);
    }
    
    public String translate(M key, Object... placeholders) {
        return getCoLang(locale).translate(key, placeholders);
    }
    
    public String translate(M key, Locale locale) {
        return getCoLang(locale).translate(key);
    }
    
    public String translate(M key, Locale locale, Object... placeholders) {
        return getCoLang(locale).translate(key, placeholders);
    }
    
}
