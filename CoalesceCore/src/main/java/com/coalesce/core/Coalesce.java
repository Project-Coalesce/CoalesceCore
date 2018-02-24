package com.coalesce.core;

import com.coalesce.core.plugin.ICoPlugin;
import com.coalesce.core.text.ActionBar;
import com.coalesce.core.text.BossBar;
import com.coalesce.core.text.Text;
import com.coalesce.core.text.Title;
import com.coalesce.core.text.Toast;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Static access to the CoalesceCore API
 */
public final class Coalesce {
    
    private static ICoPlugin coalesce;
    
    private Coalesce() {}
    
    public static void setCoalesce(ICoPlugin coalesce) {
        if (Coalesce.coalesce != null) {
            throw new UnsupportedOperationException("Can't define a new Coalesce when its already defined.");
        }
        else Coalesce.coalesce = coalesce;
    }
    
    public static void sendTitle(Player player, Title title) {
        player.sendTitle(title.getTitle(), title.getSubTitle(), title.fadeInTicks(), title.durationTicks(), title.fadeOutTicks());
    }
    
    public static void sendMessage(Player player, Text text) {
        try {
            Object craftPlayer = player.getClass().getMethod("getHandle").invoke(player);
            Object connection = craftPlayer.getClass().getField("playerConnection").get(craftPlayer);
            Class<?> baseComponent = getNMSClass("IChatBaseComponent");
            Class<?> serializer = getNMSClass("IChatBaseComponent$ChatSerializer");
            Class<?> chatPacket = getNMSClass("PacketPlayOutChat");
            Constructor packet = chatPacket.getConstructor(baseComponent, getNMSClass("ChatMessageType"));
        
            Object component = serializer.getDeclaredMethod("a", String.class).invoke(null, text.toString());
            connection.getClass().getMethod("sendPacket", getNMSClass("Packet")).invoke(connection, packet.newInstance(component, getChatMessageType((byte)0)));
        }
        catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | NoSuchFieldException | InstantiationException e) {
            e.getCause().printStackTrace();
        }
    }
    
    private static Object getChatMessageType(byte type) {
        Class<?> chatMessage = getNMSClass("ChatMessageType");
        try {
            return chatMessage.getMethod("a", byte.class).invoke(null, type);
        }
        catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static void sendActionBar(Player player, ActionBar actionBar) {
    
    }
    
    public static void sendBossBar(Player player, BossBar bossBar) {
    
    }
    
    public static void sendToast(Player player, Toast toast) {
    
    }
    
    public static Class<?> getNMSClass(String className) {
        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        try {
            return Class.forName("net.minecraft.server." + version + "." + className);
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static Class<?> getOBCClass(String className) {
        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        try {
            return Class.forName("org.bukkit.craftbukkit." + version + "." + className);
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
