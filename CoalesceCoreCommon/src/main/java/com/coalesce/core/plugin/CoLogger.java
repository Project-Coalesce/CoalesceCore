package com.coalesce.core.plugin;

import static com.coalesce.core.Color.*;

public final class CoLogger {
	
	private ICoPlugin plugin;
	
	public CoLogger(ICoPlugin plugin) {
		this.plugin = plugin;
	}
	
	/**
	 * Logs to console with logging level as INFO.
	 *
	 * @param message The message to log.
	 */
	public void info(String message){
		LogLevel.INFO.log(plugin, message);
	}
	
	/**
	 * Logs to console with logging level as WARN.
	 *
	 * @param message The message to log.
	 */
	public void warn(String message){
		LogLevel.WARN.log(plugin, message);
	}
	
	/**
	 * Logs to console with logging level as ERROR.
	 *
	 * @param message The message to log.
	 */
	public void error(String message){
		LogLevel.ERROR.log(plugin, message);
	}
	
	/**
	 * Logs to console with logging level as DEBUG.
	 *
	 * @param message The message to log.
	 */
	public void debug(String message){
		LogLevel.DEBUG.log(plugin, message);
	}
	
	enum LogLevel {
		
		INFO(WHITE + "Info"),
		DEBUG(BLUE + "Debug"),
		WARN(YELLOW + "Warn"),
		ERROR(RED + "Error");
		
		private final String prefix;
		
		LogLevel(String prefix){
			this.prefix = GRAY + "[" + AQUA + "%s " + prefix + GRAY + "]" + RESET;
		}
		
		public String getPrefix(){
			return prefix;
		}
		
		public void log(ICoPlugin plugin, String message) {
			System.out.print(String.format(prefix, plugin.getDisplayName()) + ' ' + message);
		}
	}
	
}
