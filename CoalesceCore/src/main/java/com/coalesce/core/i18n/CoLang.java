package com.coalesce.core.i18n;

import com.coalesce.core.Color;
import com.coalesce.core.config.base.IConfig;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Stream;

public class CoLang<M extends Enum & Translatable> {
    
    private final Map<M, String> keyMap;
    private final IConfig langfile;
    private char colorChar;
    private Locale locale;
    
    public CoLang(IConfig langfile, LocaleStore<M> localeStore, Locale locale) {
        super();
        this.keyMap = new HashMap<>();
        this.langfile = langfile;
        this.colorChar = '&';
        this.locale = locale;
        Class<M> type = localeStore.getKeysClass();
        
        Stream.of(type.getEnumConstants()).forEach(k -> keyMap.put(k, k.format(langfile.getString(k.getKey()))));
    }

    public Locale getLocale() {
        return locale;
    }

    public String getValue(M key) {
        return keyMap.get(key);
    }
    
    public IConfig getLangfile() {
        return langfile;
    }
    
    public Map<M, String> getKeyMap() {
        return keyMap;
    }
    
    public char getColorChar() {
        return colorChar;
    }
    
    public void setColorChar(char colorChar) {
        this.colorChar = colorChar;
    }
    
    public String translate(M key) {
        return Color.translate(colorChar, getValue(key));
    }
    
    public String translate(M key, Object... placeholders) {
        return Color.translate(colorChar, langfile.getPlugin().getCoFormatter().formatString(getValue(key), placeholders));
    }
    
}
