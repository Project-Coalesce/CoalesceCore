package com.coalesce.core.update;

import com.coalesce.core.plugin.ICoPlugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadUpdateThread extends Thread {
	
	public final ICoPlugin plugin;
	private final URL downloadUrl;
	private final IUpdateLogger logger;
	
	private String name;
	
	public HttpURLConnection connection;
	private FileOutputStream outputStream;
	public long downloaded;
	
	private static final byte[] BUFFER = new byte[1024];
	
	public DownloadUpdateThread(ICoPlugin plugin, IUpdateLogger logger, URL downloadUrl, String name) throws Exception {
		
		this.plugin = plugin;
		this.logger = logger;
		this.downloadUrl = downloadUrl;
		
		this.name = name;
		downloaded = 0L;
		
		setName("Auto-Update");
		setDaemon(false);
	}
	
	@Override
	public void run() {
		
		try{
			connection = (HttpURLConnection) downloadUrl.openConnection();
			connection.setRequestProperty("User-Agent", plugin.getRealName() + " Spigot Plugin");
			
			//Create a temp file. We dont want to delete the plugin if it is still in use, or if the download fails
			File path = new File(plugin.getPluginFolder().getParentFile() + File.separator + "Updates");
			path.mkdirs();
			File tempDownloadFile = new File(path + File.separator + name);
			tempDownloadFile.createNewFile();
			plugin.setUpdateFile(tempDownloadFile);
			
			logger.sizePush(connection.getContentLengthLong());
			
			InputStream in = connection.getInputStream();
			outputStream = new FileOutputStream(tempDownloadFile);
			
			plugin.getCoLogger().info("Downloading update...");
			
			logger.start();
			int count;
			while ((count = in.read(BUFFER, 0, 1024)) != -1) {
				outputStream.write(BUFFER, 0, count);
				downloaded += count;
				logger.progressPush(downloaded);
			}
			logger.stop();
			
			outputStream.close();
			in.close();
			
			//Unload the plugin
			//plugin.getPluginUtils().unload(plugin);
			
			//Enable the new jar
			//ICoPlugin updated = plugin.getPluginUtils().load(tempDownloadFile);
			if (tempDownloadFile.exists()){
				plugin.getCoLogger().info("Successfully downloaded update! Installing on next restart.");
			}
			else {
				plugin.setUpdateNeeded(false);
				throw new RuntimeException("Could not locate the new update jar file... aborting update process.");
			}
			
		} catch (Exception e) {
			if (outputStream != null) try { outputStream.close(); } catch (Exception ignored) {}
			e.printStackTrace();
		}
	}
}
