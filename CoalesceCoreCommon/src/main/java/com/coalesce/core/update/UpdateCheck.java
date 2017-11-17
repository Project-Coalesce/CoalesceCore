package com.coalesce.core.update;

import com.coalesce.core.http.CoHTTP;
import com.coalesce.core.plugin.ICoPlugin;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public final class UpdateCheck implements Runnable {
	
	private final IUpdateLogger logger;
	private final boolean autoUpdate;
	private final ICoPlugin plugin;
	private final String owner;
	private final String repo;
	private UpdateData data;
	
	//First is owner string, then repo
	private static final String GITHUB_ADDRESS = "https://api.github.com/repos/%s/%s/releases/latest";
	
	public UpdateCheck(ICoPlugin plugin, IUpdateLogger logger, String owner, String repo, boolean autoUpdate) {
		this.autoUpdate = autoUpdate;
		this.logger = logger;
		this.plugin = plugin;
		this.owner = owner;
		this.repo = repo;
		
	}
	
	@Override
	public void run() {
		plugin.getCoLogger().info("Looking for updates to " + plugin.getRealName() + "...");
		
		String stringData = null;
		try {
			stringData = CoHTTP.sendGet(String.format(GITHUB_ADDRESS, owner, repo), plugin.getRealName() + " Spigot Plugin").get();
		}
		catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		try {
			this.data = new Gson().fromJson(stringData, UpdateData.class);
			
			if (!plugin.getVersion().matches(data.getVersion())) {
				plugin.setUpdateNeeded(true);
				plugin.getCoLogger().info("New version was found! [" + data.getVersion() + "]");
				
				if (autoUpdate){
					List<Asset> javaAssets = data.assets.stream().filter(check -> check.assetName.substring((check.assetName.length() - 3)).equalsIgnoreCase("jar")).collect(Collectors.toList());
					
					if(javaAssets.size() == 0){
						plugin.getCoLogger().info(String.format("No jars were found in the release \"%s\". Aborting auto-update. You can update manually.", data.getUrl()));
						return;
					}else if(javaAssets.size() == 1){
						Asset download = javaAssets.get(0);
						
						new DownloadUpdateThread(plugin, logger, new URL(download.downloadURL), download.assetName).start();
						return;
					}
					
					List<Asset> labeledAssets = javaAssets.stream().filter(check -> check.label != null && check.label.equals("Auto-Download")).collect(Collectors.toList());
					
					if(labeledAssets.size() != 1){
						plugin.getCoLogger().info(String.format("More than one possible jar was found in the release \"%s\". Aborting auto-update. You can update manually.", data.getUrl()));
						return;
					}
					
					Asset download = labeledAssets.get(0);
					new DownloadUpdateThread(plugin, logger, new URL(download.downloadURL), download.assetName).start();
				} else {
					plugin.getCoLogger().info("Download it at: " + data.getUrl());
				}
				
				return;
			}
			plugin.getCoLogger().info(plugin.getRealName() + " is up to date.");
		}
		catch (NullPointerException e) {
			plugin.getCoLogger().warn("Could not find latest released version from GitHub. (This plugin may not have a public release yet)");
		}
		catch (Exception e) {
			plugin.getCoLogger().warn("There was an error checking for updates.");
		}
		
	}
	
	
	private static class UpdateData {
		
		private String message;
		
		private List<Asset> assets;
		
		@SerializedName( "html_url" )
		private String url;
		
		@SerializedName( "tag_name" )
		private String version;
		
		/**
		 * Gets the assets from the resource post.
		 *
		 * @return The assets.
		 */
		public List<Asset> getAssets(){
			return assets;
		}
		
		/**
		 * Gets the HTML URL to redirect for the user.
		 *
		 * @return URL in string.
		 */
		public String getUrl(){
			return url;
		}
		
		/**
		 * Gets a message to check if the page exists or not.
		 * <p>
		 * <p>This is mainly used to check for missing releases.</p>
		 * <p>
		 * <p>If no releases exist the message will be "Not Found", otherwise the message will be null</p>
		 *
		 * @return A message if no release is found.
		 */
		public String getMessage() {
			return message;
		}
		
		/**
		 * Gets the tagged version for this release.
		 *
		 * @return The release version.
		 */
		public String getVersion() {
			return version;
		}
	}
	
	private static class Asset {
		
		@SerializedName( "browser_download_url")
		private String downloadURL;
		
		@SerializedName("name")
		private String assetName;
		
		private String label;
		
	}
}
