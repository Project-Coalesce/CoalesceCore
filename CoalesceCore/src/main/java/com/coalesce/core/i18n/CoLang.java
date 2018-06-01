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
        this.keyMap = new HashMap<>();
        this.langfile = langfile;
        this.colorChar = '&';
        this.locale = locale;
        Class<M> type = localeStore.getKeysClass();
        Stream.of(type.getEnumConstants()).forEach(k -> {
            if (!langfile.contains(k.getKey(), true)) langfile.getPlugin().getCoLogger().warn("Could not find key \"" + k.getKey() + "\" in " + langfile.getName());
            else keyMap.put(k, k.format(langfile.getString(k.getKey())));
        });
    }
    
    /**
     * Gets the locale represented by this CoLang
     * @return The locale this CoLang represents
     */
    public Locale getLocale() {
        return locale;
    }
    
    /**
     * Gets a key value from this CoLang.
     * @param key The key to look for
     * @return The corresponding key. Null if it doesnt exist
     */
    public String getValue(M key) {
        return keyMap.get(key);
    }
    
    /**
     * Gets the language file associated with this CoLang
     * @return The file where this CoLangs keys are initially stored
     */
    public IConfig getLangfile() {
        return langfile;
    }
    
    /**
     * Gets a key map of all the keys and the messages associated with them.
     * @return The key map.
     */
    public Map<M, String> getKeyMap() {
        return keyMap;
    }
    
    /**
     * Sets the color char of this CoLang. Color codes will have this character as a prefix. (Default is '&')
     * @return The color char prefix.
     */
    public char getColorChar() {
        return colorChar;
    }
    
    /**
     * Get the character needed to prefix a color code (Default is '&')
     * @param colorChar The new color char
     */
    public void setColorChar(char colorChar) {
        this.colorChar = colorChar;
    }
    
    /**
     * Gets a key from this CoLang
     * @param key The key to translate
     * @return The translated key if it exists in the keyMap, null otherwise.
     */
    public String translate(M key) {
        return keyMap.containsKey(key) ? Color.translate(colorChar, getValue(key)) : null;
    }
    
    /**
     * Gets a key from this CoLang and replaces placeholders
     * @param key The key to translate
     * @param placeholders The placeholders to put in place
     * @return The translated key if it exists in the keyMap, null otherwise.
     */
    public String translate(M key, Object... placeholders) {
        return keyMap.containsKey(key) ? Color.translate(colorChar, langfile.getPlugin().getCoFormatter().formatString(getValue(key), placeholders)) : null;
    }
    
}
