package com.coalesce.core.i18n;

/**
 * This tells the core that this Enum is a Translatable Locale and should be used as that.
 * <p>The implementation of this interface is very specific and must be followed strictly.
 */
public interface Translatable {
    
    /**
     * <p>--==IMPLEMENTATION==--</p>
     * <p>This method should return the first value in the enum constructor.<p/>
     *
     * Gets the configuration key related to this MessageKey
     * @return The configuration key
     */
    String getKey();
    
    /**
     * <p>--==IMPLEMENTATION==--</p>
     * <p>This method should return the values following the key in the constructor.<p/>
     *
     * Gets all the placeholders for this MessageKey
     * @return The placeholders of this message
     */
    String[] getHolders();
    
    /**
     * This formats the key value to a usable state for proper placeholder parsing.
     * @param string The key value
     * @return The formatted key
     */
    default String format(String string) {
        String formatted = string;
        for (int i = 0; getHolders().length > i; i++) {
            formatted = formatted.replaceAll(getHolders()[i], "{" + i + "}");
        }
        return formatted;
    }
    
}
