package com.coalesce.core.update;

import com.coalesce.core.plugin.ICoPlugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadThread extends Thread {
    
    private final ICoPlugin plugin;
    private final URL downloadUrl;
    private final String filename;
    private final File location;
    
    private FileOutputStream outputStream;
    
    private static final byte[] BUFFER = new byte[1024];
    
    DownloadThread(ICoPlugin plugin, URL downloadUrl, File location, String fileName) {
        this.downloadUrl = downloadUrl;
        this.filename = fileName;
        this.location = location;
        this.plugin = plugin;
        
        setName("UpdateDownloader");
        setDaemon(false);
    }
    
    @Override
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void run() {
        try {
    
            HttpURLConnection connection = (HttpURLConnection)downloadUrl.openConnection();
            connection.setRequestProperty("User-Agent", plugin.getRealName() + " Plugin Auto Update");
            
            if (!location.exists()) {
                location.mkdirs();
            }
            File downloadedFile = new File(location + File.separator + filename);
            downloadedFile.createNewFile();
    
            InputStream in = connection.getInputStream();
            outputStream = new FileOutputStream(downloadedFile);
            
            int count;
            while ((count = in.read(BUFFER, 0, 1024)) != -1) {
                outputStream.write(BUFFER, 0, count);
            }
            
            outputStream.close();
            in.close();
            
        } catch (Exception e) {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (Exception ignored) {
                }
            }
            e.printStackTrace();
        }
    }
}
