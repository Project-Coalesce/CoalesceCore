package com.coalesce.core.i18n;

import com.coalesce.core.config.base.IConfig;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CoLang<M extends Enum> {
    
    private final Map<M, String> keyMap;
    private final IConfig langfile;
    private Locale locale;
    
    public CoLang(IConfig langfile) {
        this.keyMap = new HashMap<>();
        this.langfile = langfile;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
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
    
}
