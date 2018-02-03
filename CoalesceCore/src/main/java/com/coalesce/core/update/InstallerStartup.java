package com.coalesce.core.update;

import com.coalesce.core.plugin.ICoPlugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class InstallerStartup {
    
    private final ICoPlugin plugin;
    private final List<String> files;
    private final File pluginFolder, updateFolder, updater;
    
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public InstallerStartup(ICoPlugin plugin) {
        this.plugin = plugin;
        this.files = new ArrayList<>();
        this.pluginFolder = plugin.getPluginsDirectory();
        this.updateFolder = new File(pluginFolder + File.separator + "Updates");
        this.updater = new File(updateFolder + File.separator + "Coalescing.jar");
    
        try {
            if (!updater.exists()) {
                if (!updateFolder.exists()) updateFolder.mkdirs();
                updater.createNewFile();
            }
    
            InputStream stream;
            OutputStream resStreamOut;
            
            stream = getClass().getResourceAsStream("/Coalescing.jar");
    
            int readBytes;
            byte[] buffer = new byte[4096];
            resStreamOut = new FileOutputStream(updater);
            while ((readBytes = stream.read(buffer)) > 0) {
                resStreamOut.write(buffer, 0, readBytes);
            }
            stream.close();
            resStreamOut.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    @SuppressWarnings("all")
    public void addUpdate(String oldFileName, String newFileName) {
        plugin.getCoLogger().info(oldFileName + " will be updated to " + newFileName + " upon restart.");
        files.add(oldFileName + ":" + newFileName.concat(" "));
    }
    
    public void start() {
        if (!updater.exists()) {
            plugin.getCoLogger().error("Aborting update installs... Could not locate Coalescing.jar. Has it been renamed?");
            return;
        }
        StringBuilder builder = new StringBuilder();
        builder.append("java -jar ").append(updater.getAbsoluteFile()).append(" ").append(pluginFolder.getAbsoluteFile()).append(" ").append(updateFolder.getAbsoluteFile()).append(" ");
        files.forEach(builder::append);
        
        try {
            Runtime.getRuntime().exec(builder.toString());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
