package com.coalesce.core.update;

import com.coalesce.core.http.CoHTTP;
import com.coalesce.core.plugin.ICoPlugin;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class UpdateLookup implements Runnable {
    
    /*
    How i think this should be setup.
    
    Do an update check:
        - Look in the repo and find an update. If an update is found, download the update with the download thread.
    
    Have a downloader thread. When ran, have a download location option in the constructor and download it to the specific area.
    
    
     */
    
    private static final String GITHUB_ADDRESS = "https://api.github.com/repos/%s/%s/releases/latest";
    
    private final InstallerStartup installer;
    private final String currentVersion;
    private final File downloadLocation;
    private final boolean autoUpdate;
    private final ICoPlugin plugin;
    private final String owner;
    private final String repo;
    
    public UpdateLookup(ICoPlugin plugin, InstallerStartup installer, String owner, String repo, String currentVersion, boolean autoDownload) {
        this.downloadLocation = new File(plugin.getPluginFolder().getParentFile() + File.separator + "Updates");
        this.currentVersion = currentVersion;
        this.autoUpdate = autoDownload;
        this.installer = installer;
        this.plugin = plugin;
        this.owner = owner;
        this.repo = repo;
    }
    
    @Override
    public void run() {
    
        String stringData = null;
        try {
            stringData = CoHTTP.sendGet(String.format(GITHUB_ADDRESS, owner, repo), plugin.getRealName() + " Spigot Plugin").get();
        }
        catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        try {
            UpdateData data = new Gson().fromJson(stringData, UpdateData.class);
        
            if (currentVersion == null || !currentVersion.matches(data.getVersion())) {
                
                
                
                if (autoUpdate) {
                    List<Asset> javaAssets = data.assets.stream().filter(check -> check.assetName.substring((check.assetName.length() - 3)).equalsIgnoreCase("jar")).collect(Collectors.toList());
                
                    if (javaAssets.size() == 0) {
                        System.out.println("Cannot find a jar to download! Download the update manually at " + data.getUrl());
                        return;
                    } else if (javaAssets.size() == 1) {
                        Asset download = javaAssets.get(0);
                        
                        installer.addUpdate(plugin.getPluginJar().getName(), download.assetName);
                    
                        new DownloadThread(plugin, new URL(download.downloadURL), downloadLocation, download.assetName).start();
                        return;
                    }
                
                    List<Asset> labeledAssets = javaAssets.stream().filter(check -> check.label != null && check.label.equals("Auto-Download")).collect(Collectors.toList());
                
                    if (labeledAssets.size() != 1) {
                        System.out.println(String.format("More than one possible jar was found in the release \"%s\". Aborting auto-update. You can update manually.", data.getUrl()));
                        return;
                    }
                
                    Asset download = labeledAssets.get(0);
                    new DownloadThread(plugin, new URL(download.downloadURL), downloadLocation, download.assetName).start();
                } else {
                    System.out.println("Download it at: " + data.getUrl());
                }
                return;
            }
            System.out.println(plugin.getRealName() + " is up to date.");
        }
        catch (NullPointerException e) {
            System.out.println("Could not find latest released version from GitHub. (This plugin may not have a public release yet)");
        }
        catch (Exception e) {
            System.out.println("There was an error checking for updates.");
        }
    }
    
    @SuppressWarnings("unused")
    private static class UpdateData {
        
        private String message;
        
        private List<Asset> assets;
        
        @SerializedName( "html_url" ) private String url;
        
        @SerializedName( "tag_name" ) private String version;
        
        /**
         * Gets the assets from the resource post.
         *
         * @return The assets.
         */
        public List<Asset> getAssets() {
            return assets;
        }
        
        /**
         * Gets the HTML URL to redirect for the user.
         *
         * @return URL in string.
         */
        public String getUrl() {
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
    
    @SuppressWarnings("unused")
    private static class Asset {
        
        @SerializedName( "browser_download_url" ) private String downloadURL;
        
        @SerializedName( "name" ) private String assetName;
        
        private String label;
        
    }
    
}
