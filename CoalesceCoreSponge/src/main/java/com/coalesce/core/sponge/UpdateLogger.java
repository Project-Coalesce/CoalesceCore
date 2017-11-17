package com.coalesce.core.sponge;

import com.coalesce.core.plugin.ICoPlugin;
import com.coalesce.core.update.IUpdateLogger;
import org.spongepowered.api.scheduler.Task;

public final class UpdateLogger implements IUpdateLogger, Runnable {
	
	private final ICoPlugin plugin;
	private final Task.Builder task;
	private Task task1;
	private long downloaded;
	private String totalSize;
	private long local;
	
	public UpdateLogger(ICoPlugin plugin) {
		this.plugin = plugin;
		this.task = Task.builder()
				.async()
				.delayTicks(20)
				.intervalTicks(20)
				.execute(this);
	}
	
	@Override
	public void stop() {
		task1.cancel();
	}
	
	@Override
	public void start() {
		task1 = task.submit(plugin);
	}
	
	@Override
	public void sizePush(long size) {
		this.totalSize = bytes(size);
	}
	
	@Override
	public void progressPush(long downloaded) {
		this.downloaded = downloaded;
	}
	
	@Override
	public void run() {
		long downloadedThisSecond = downloaded - local;
		local = downloaded;
		
		plugin.getCoLogger().info(String.format("Download progress: %s/%s @%s/s", bytes(downloaded), totalSize, bytes(downloadedThisSecond)));
	}
}
