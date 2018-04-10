package com.coalesce.core.chat;

import com.coalesce.core.plugin.ICoPlugin;

import java.util.Arrays;
import java.util.List;

import static com.coalesce.core.Color.*;

public final class CoFormatter {
    
    private final ICoPlugin plugin;
    
    //Constants
    private final static int CENTER_PX = 154;
    
    public CoFormatter(ICoPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Formats a plugin message
     *
     * @param message The message to format
     * @return The formatted message.
     */
    public String format(String message) {
        return GRAY + "[" + RESET + plugin.getDisplayName() + GRAY + "]" + RESET + " " + message;
    }
    
    /**
     * Formats a plugin message
     *
     * @param message The message to format
     * @param objects The objects to replace the placeholders with
     * @return The formatted message.
     */
    public String format(String message, Object... objects) {
        return GRAY + "[" + RESET + plugin.getDisplayName() + GRAY + "]" + RESET + " " + formatString(message, objects);
    }
    
    /**
     * Formats a message and replaces placeholders
     *
     * @param message The message to format
     * @param objects The objects used to replace the placeholders
     * @return The formatted string
     * <p>
     * Example placeholders: <code>formatString("Hello, I am {0}, a {1} year old person.", "NJDaeger", "100")</code>
     * <p>that would return "Hello, I am NJDaeger, a 100 year old person.</p>
     */
    public String formatString(String message, Object... objects) {
        List<Object> fillers = Arrays.asList(objects);
        for (int i = 0; i < fillers.size(); i++) {
            if (fillers.get(i) != null) {
                message = message.replace("{" + i + "}", fillers.get(i).toString());
            }
        }
        return message;
    }
    
    /**
     * Centers a string for chat.
     *
     * @param message The message to center in chat.
     * @return A centered string.
     */
    public String centerString(String message) {
        if (message == null || message.equals("")) {
            return "";
        }
        message = translate('&', message);
        
        int messagePxSize = getWidth(message);
        
        int halvedMessageSize = messagePxSize / 2;
        int toCompensate = CENTER_PX - halvedMessageSize;
        int spaceLength = FontInfo.getCharSize(' ');
        int compensated = 0;
        StringBuilder sb = new StringBuilder();
        while (compensated < toCompensate) {
            sb.append(" ");
            compensated += spaceLength;
        }
        return sb.toString() + message;
    }
    
    /**
     * Gets the width of the string message.
     *
     * @param message The message to get the width of.
     * @return The width (in pixels) of the string in minecraft.
     */
    public int getWidth(String message) {
        int messagePxSize = 0;
        boolean previousCode = false;
        boolean isBold = false;
        
        for (char c : message.toCharArray()) {
            if (c == CHAR) {
                previousCode = true;
            } else if (previousCode) {
                previousCode = false;
                isBold = c == 'l' || c == 'L';
            } else {
                messagePxSize += FontInfo.getCharSize(c, isBold);
            }
        }
        return messagePxSize;
    }
    
    /**
     * Alternate color codes in a string, if the chars variable is null then it will use a rainbow effect.
     * If string already contains color codes, they will be stripped.
     *
     * @param str   String to add color to.
     * @param chars Colors that will be alternated in the string, if null then its rainbow.
     * @return Changed String
     */
    public String rainbowifyString(String str, char... chars) {
        str = stripColor(str);
        if (chars == null || chars.length == 0) {
            chars = new char[]{'c', '6', 'e', 'a', 'b', '3', 'd'};
        }
        
        int index = 0;
        StringBuilder returnValue = new StringBuilder();
        for (char c : str.toCharArray()) {
            returnValue.append("&").append(chars[index]).append(c);
            index++;
            if (index == chars.length) {
                index = 0;
            }
        }
        return translate('&', returnValue.toString());
    }

}
