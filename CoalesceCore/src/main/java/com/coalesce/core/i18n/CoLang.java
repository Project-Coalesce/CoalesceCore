package com.coalesce.core.i18n;

import com.coalesce.core.Color;
import com.coalesce.core.config.base.IConfig;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Stream;

public class CoLang<M extends Enum & Translatable> {
    
    private final Map<M, String> keyMap;
    private final IConfig langfile;
    private char colorChar;
    private Locale locale;
    private Class<M> type;

    @SuppressWarnings("unchecked")
    public CoLang(IConfig langfile, Locale locale) {
        this.keyMap = new HashMap<>();
        this.langfile = langfile;
        this.colorChar = '&';
        this.locale = locale;
        //ugly, but i dont have to put the class type in the parameter!!
        this.type = (Class<M>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        
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
