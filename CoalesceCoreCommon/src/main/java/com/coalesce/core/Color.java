package com.coalesce.core;

import org.fusesource.jansi.Ansi;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public enum Color {
    
    /**
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#000000;float:right;margin: 0 10px 0 0"></div>
     */
    BLACK('0', "black", Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.BLACK).toString()),
    /**
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#0000AA;float:right;margin: 0 10px 0 0"></div>
     */
    DARK_BLUE('1', "dark_blue", Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.BLUE).toString()),
    /**
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#00AA00;float:right;margin: 0 10px 0 0"></div>
     */
    DARK_GREEN('2', "dark_green", Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.GREEN).toString()),
    /**
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#00AAAA;float:right;margin: 0 10px 0 0"></div>
     */
    AQUA('3', "dark_aqua", Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.CYAN).toString()),
    /**
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#AA0000;float:right;margin: 0 10px 0 0"></div>
     */
    DARK_RED('4', "dark_red", Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.RED).toString()),
    /**
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#AA00AA;float:right;margin: 0 10px 0 0"></div>
     */
    PURPLE('5', "dark_purple", Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.MAGENTA).toString()),
    /**
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#FFAA00;float:right;margin: 0 10px 0 0"></div>
     */
    GOLD('6', "gold", Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.YELLOW).toString()),
    /**
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#AAAAAA;float:right;margin: 0 10px 0 0"></div>
     */
    SILVER('7', "gray", Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.WHITE).toString()),
    /**
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#555555;float:right;margin: 0 10px 0 0"></div>
     */
    GRAY('8', "dark_gray", Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.BLACK).bold().toString()),
    /**
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#5555FF;float:right;margin: 0 10px 0 0"></div>
     */
    BLUE('9', "blue", Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.BLUE).bold().toString()),
    /**
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#55FF55;float:right;margin: 0 10px 0 0"></div>
     */
    GREEN('a', "green", Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.GREEN).bold().toString()),
    /**
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#55FFFF;float:right;margin: 0 10px 0 0"></div>
     */
    LIGHT_BLUE('b', "aqua", Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.CYAN).bold().toString()),
    /**
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#FF5555;float:right;margin: 0 10px 0 0"></div>
     */
    RED('c', "red", Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.RED).bold().toString()),
    /**
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#FF55FF;float:right;margin: 0 10px 0 0"></div>
     */
    MAGENTA('d', "light_purple", Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.MAGENTA).bold().toString()),
    /**
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#FFFF55;float:right;margin: 0 10px 0 0"></div>
     */
    YELLOW('e', "yellow", Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.YELLOW).bold().toString()),
    /**
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#FFFFFF;float:right;margin: 0 10px 0 0"></div>
     */
    WHITE('f', "white", Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.WHITE).bold().toString()),
    MAGIC('k', "obfuscated", Ansi.ansi().a(Ansi.Attribute.BLINK_SLOW).toString()),
    BOLD('l', "bold", Ansi.ansi().a(Ansi.Attribute.UNDERLINE_DOUBLE).toString()),
    STRIKE('m', "strikethrough", Ansi.ansi().a(Ansi.Attribute.STRIKETHROUGH_ON).toString()),
    UNDERLINE('n', "underline", Ansi.ansi().a(Ansi.Attribute.UNDERLINE).toString()),
    ITALICS('o', "italics", Ansi.ansi().a(Ansi.Attribute.ITALIC).toString()),
    RESET('r', "reset", Ansi.ansi().a(Ansi.Attribute.RESET).toString());
    
    private char code;
    private String ansi;
    private String componentCode;
    public static final char CHAR = '\u00A7';
    private static final Pattern STRIP_COLOR_PATTERN = Pattern.compile("(?i)" + String.valueOf(CHAR) + "[0-9A-FK-OR]"); //Bukkit
    
    Color(char code, String componentCode, String ansi) {
        this.code = code;
        this.ansi = ansi;
        this.componentCode = componentCode;
    }
    
    @Override
    public String toString() {
        return new String(new char[]{CHAR, code});
    }
    
    /**
     * Gets the color code name for textcomponents
     * @return The TextComponent code.
     */
    public String getComponentCode() {
        return componentCode;
    }
    
    /**
     * Gets the char code this color code uses
     *
     * @return The color code
     */
    public char getCode() {
        return this.code;
    }
    
    /**
     * Gets the ansi equivalent of this color
     *
     * @return The ansi color
     */
    public String getAnsi() {
        return ansi;
    }
    
    /**
     * @param prefix  The prefix of the color codes indicating a color code
     * @param message The message to be translated
     * @return The translated message.
     */
    public static String translate(char prefix, String message) {
        //From Bukkit
        char[] b = message.toCharArray();
        for (int i = 0; i < b.length - 1; i++) {
            if (b[i] == prefix && "0123456789AaBbCcDdEeFfKkLlMmNnOoRr".indexOf(b[i + 1]) > -1) {
                b[i] = Color.CHAR;
                b[i + 1] = Character.toLowerCase(b[i + 1]);
            }
        }
        return new String(b);
        //From Bukkit End
    }
    
    /**
     * Creates an array of all the color codes in the given string
     *
     * @param prefix The prefix to the color codes indicating a color code
     * @param text   The text to look for the color codes
     * @return An array of color
     */
    public static Color[] getColorArray(char prefix, String text) {
        Set<Color> colors = new HashSet<>();
        
        char[] b = text.toCharArray();
        for (int i = 0; i < b.length - 1; i++) {
            if (b[i] == prefix && "0123456789AaBbCcDdEeFfKkLlMmNnOoRr".indexOf(b[i + 1]) > -1) {
                colors.add(getColorFromChar(b[i + 1]));
            }
        }
        return colors.toArray(Color.values());
    }
    
    /**
     * Gets a color from a character value
     *
     * @param value The character to check
     * @return The color if exists, otherwise its the reset color
     */
    public static Color getColorFromChar(char value) {
        Color color = Color.RESET;
        if ("0123456789AaBbCcDdEeFfKkLlMmNnOoRr".indexOf(value) == -1) {
            return color;
        }
        for (Color c : Color.values()) {
            if (c.getCode() == value) {
                color = c;
                break;
            }
        }
        return color;
    }
    
    /**
     * Strips a string of all color codes //From Bukkit
     *
     * @param input Input String
     * @return The input without color codes.
     */
    public static String stripColor(String input) {
        if (input == null) {
            return null;
        }
        return STRIP_COLOR_PATTERN.matcher(input).replaceAll("");
    }
    
    public static String toConsoleColor(char prefix, String input) {
        String translated = translate(prefix, input);
        for (Color color : values()) {
            translated = translated.replace(color.toString(), color.getAnsi());
        }
        return translated;
    }
    
}
