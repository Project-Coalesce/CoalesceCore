package com.coalesce.core.i18n;

import com.coalesce.core.config.YmlConfig;
import com.coalesce.core.config.base.IConfig;
import com.coalesce.core.plugin.ICoPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@SuppressWarnings({"unused", "WeakerAccess"})
public final class LocaleStore<M extends Enum & Translatable> {
    
    private final Map<Locale, CoLang<M>> localeMap;
    private Class<M> messageKeysClass;
    private final ICoPlugin<M> plugin;
    private Locale locale;
    
    public LocaleStore(ICoPlugin<M> plugin) {
        this(plugin, Locale.ENGLISH);
    }
    
    public LocaleStore(ICoPlugin<M> plugin, Locale locale) {
        this.localeMap = new HashMap<>();
        this.locale = locale;
        this.plugin = plugin;
    }
    
    public void setClassType(Class<M> cls) {
        if (messageKeysClass != null) throw new UnsupportedOperationException("Cannot set a new LocaleStore class type after it's been set.");
        this.messageKeysClass = cls;
    }
    
    private void checkClassIsSet() {
        if (messageKeysClass == null) throw new RuntimeException("Must set the LocaleStore class type before using locales.");
    }
    
    /**
     * Gets a CoLang from the corresponding locale.
     * @param locale The locale to get the CoLang from
     * @return The CoLang if it is loaded and exists. Null otherwise.
     */
    public CoLang<M> getCoLang(Locale locale) {
        checkClassIsSet();
        return localeMap.get(locale);
    }
    
    public void loadCoLang(IConfig file, Locale locale) {
        loadCoLang(new CoLang<>(file, this, locale));
    }
    
    public void loadCoLang(File file, Locale locale) {
        String name = file.getAbsolutePath();
        loadCoLang(name.contains(".") ? name.substring(0, name.lastIndexOf(".")) : name, locale);
    }
    
    public void loadCoLang(String file, Locale locale) {
        loadCoLang(new YmlConfig(file, plugin), locale);
    }
    
    /**
     * Loads a CoLang into the LocaleStore
     * @param coLang The CoLang to load.
     */
    public void loadCoLang(CoLang<M> coLang) {
        checkClassIsSet();
        localeMap.put(coLang.getLocale(), coLang);
    }
    
    /**
     * Unloads a language from this plugins LocaleStore
     * @param locale The locale to remove.
     * @return True if it was successfully removed, false otherwise.
     */
    public boolean unLoadCoLang(Locale locale) {
        checkClassIsSet();
        return localeMap.remove(locale) != null;
    }
    
    public Class<M> getKeysClass() {
        return messageKeysClass;
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
