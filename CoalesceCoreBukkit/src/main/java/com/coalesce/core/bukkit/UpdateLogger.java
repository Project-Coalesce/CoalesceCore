package com.coalesce.core.bukkit;

import com.coalesce.core.plugin.ICoPlugin;
import com.coalesce.core.update.IUpdateLogger;
import org.bukkit.scheduler.BukkitRunnable;

public class UpdateLogger extends BukkitRunnable implements IUpdateLogger {
	
	private final ICoPlugin plugin;
	private long downloaded;
	private String totalSize;
	private long local;
	
	public UpdateLogger(ICoPlugin plugin) {
		this.plugin = plugin;
		
	}
	
	@Override
	public void run(){
		
		long downloadedThisSecond = downloaded - local;
		local = downloaded;
		
		plugin.getCoLogger().info(String.format("Download progress: %s/%s @%s/s", bytes(downloaded), totalSize, bytes(downloadedThisSecond)));
		
	}
	
	@Override
	public void stop() {
		cancel();
	}
	
	@Override
	public void start() {
		runTaskTimerAsynchronously((CoPlugin)plugin, 20L, 20L);
	}
	
	@Override
	public void sizePush(long size) {
		this.totalSize = bytes(size);
	}
	
	@Override
	public void progressPush(long downloaded) {
		this.downloaded = downloaded;
	}
}
