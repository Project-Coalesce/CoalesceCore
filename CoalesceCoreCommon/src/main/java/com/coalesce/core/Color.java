package com.coalesce.core;

import java.util.HashSet;
import java.util.Set;

public enum Color {
	
	BLACK('0'),
	DARK_BLUE('1'),
	DARK_GREEN('2'),
	AQUA('3'),
	DARK_RED('4'),
	PURPLE('5'),
	GOLD('6'),
	SILVER('7'),
	GRAY('8'),
	BLUE('9'),
	GREEN('a'),
	LIGHT_BLUE('b'),
	RED('c'),
	MAGENTA('d'),
	YELLOW('e'),
	WHITE('f'),
	MAGIC('k'),
	BOLD('l'),
	STRIKE('m'),
	UNDERLINE('n'),
	ITALICS('o'),
	RESET('r');
	
	private char code;
	private static final char CHAR = '\u00A7';
	
	Color(char code) {
		this.code = code;
	}
	
	@Override
	public String toString() {
		return new String(new char[] {CHAR, code});
	}
	
	public char getCode() {
		return this.code;
	}
	
	/**
	 *
	 * @param prefix The prefix of the color codes indicating a color code
	 * @param message
	 * @return
	 */
	public static String translate(char prefix, String message) {
		//From Bukkit
		char[] b = message.toCharArray();
		for (int i = 0; i < b.length - 1; i++) {
			if (b[i] == prefix && "0123456789AaBbCcDdEeFfKkLlMmNnOoRr".indexOf(b[i+1]) > -1) {
				b[i] = Color.CHAR;
				b[i+1] = Character.toLowerCase(b[i+1]);
			}
		}
		return new String(b);
		//From Bukkit End
	}
	
	/**
	 * Creates an array of all the color codes in the given string
	 * @param prefix The prefix to the color codes indicating a color code
	 * @param text The text to look for the color codes
	 * @return An array of color
	 */
	public static Color[] getColorArray(char prefix, String text) {
		Set<Color> colors = new HashSet<>();
		
		char[] b = text.toCharArray();
		for (int i = 0; i < b.length - 1; i++) {
			if (b[i] == prefix && "0123456789AaBbCcDdEeFfKkLlMmNnOoRr".indexOf(b[i+1]) > -1) {
				colors.add(getColorFromChar(b[i+1]));
			}
		}
		return colors.toArray(Color.values());
	}
	
	/**
	 * Gets a color from a character value
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

}
