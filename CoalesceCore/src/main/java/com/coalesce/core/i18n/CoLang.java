package com.coalesce.core.i18n;

import com.coalesce.core.config.base.IConfig;

import java.util.HashMap;
import java.util.Map;

public class CoLang<M extends Enum> {
    
    private final Map<M, String> keyMap;
    private final IConfig langfile;
    private Language language;
    
    public CoLang(IConfig langfile) {
        this.keyMap = new HashMap<>();
        this.langfile = langfile;
    }
    
    public void setLanguage(Language language) {
        this.language = language;
    }
    
    public Language getLanguage() {
        return this.language;
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
