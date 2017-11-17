package com.coalesce.core.update;

public interface IUpdateLogger {
	
	/**
	 * Stops the logger
	 */
	void stop();
	
	/**
	 * Starts the update logger.
	 */
	void start();
	
	/**
	 * pushes the size of the file
	 * @param size the file size
	 */
	void sizePush(long size);
	
	/**
	 * pushes the download progress
	 * @param downloaded the amount downloaded.
	 */
	void progressPush(long downloaded);
	
	default String bytes(long n){
		if(n <= 999) return n + "B";
		else if(n >= 1000 && n <= 999999) return (float) ((int) n / 100) / 10 + "KB";
		else if(n >= 1000000 && n <= 999999999) return (float) ((int) n / 100000) / 10 + "MB";
		else if(n >= 10000000 && n <= 999999999999L) return (float) ((int) n / 100000000) / 10 + "GB";
		else return (float) ((int) n / 100000000000L) / 10 + "TB";
	}
	
}
