package com.coalesce.core;

import com.coalesce.core.plugin.ICoPlugin;
import com.coalesce.core.text.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bukkit.Bukkit;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Static access to the CoalesceCore API
 */
public final class Coalesce {

    public static Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private static ICoPlugin coalesce;
    
    private Coalesce() {}
    
    public static void setCoalesce(ICoPlugin coalesce) {
        if (Coalesce.coalesce != null) {
            throw new UnsupportedOperationException("Can't define a new Coalesce when its already defined.");
        }
        else Coalesce.coalesce = coalesce;
    }
    
    /**
     * Sends a title to the desired player
     * @param player The player to send the title to
     * @param title The title to send
     */
    public static void sendTitle(Player player, Title title) {
        player.sendTitle(title.getTitle(), title.getSubTitle(), title.fadeInTicks(), title.durationTicks(), title.fadeOutTicks());
    }
    
    /**
     * Sends a Text message to a player
     * @param player The player to send the message to
     * @param text The message object
     */
    public static void sendMessage(Player player, Text.TextSection text) {
        sendRawMessage(player, text.toString());
    }
    
    /**
     * Sends a raw JSON message to a player
     * @param player The player to send the message to
     * @param rawJson The json message
     */
    public static void sendRawMessage(Player player, String rawJson) {
        try {
            Object craftPlayer = player.getClass().getMethod("getHandle").invoke(player);
            Object connection = craftPlayer.getClass().getField("playerConnection").get(craftPlayer);
            Class<?> baseComponent = getNMSClass("IChatBaseComponent");
            Class<?> serializer = getNMSClass("IChatBaseComponent$ChatSerializer");
            Class<?> chatPacket = getNMSClass("PacketPlayOutChat");
            Constructor packet = chatPacket.getConstructor(baseComponent, getNMSClass("ChatMessageType"));
        
            Object component = serializer.getDeclaredMethod("a", String.class).invoke(null, rawJson);
            connection.getClass().getMethod("sendPacket", getNMSClass("Packet")).invoke(connection, packet.newInstance(component, getChatMessageType((byte)0)));
        }
        catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | NoSuchFieldException | InstantiationException | ClassNotFoundException e) {
            e.getCause().printStackTrace();
        }
    }
    
    private static Object getChatMessageType(byte type) {
        try {
            Class<?> chatMessage = getNMSClass("ChatMessageType");
            return chatMessage.getMethod("a", byte.class).invoke(null, type);
        }
        catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static void sendActionBar(Player player, ActionBar actionBar) {

    }
    
    public static void sendBossBar(Player player, BossBar bossBar) {
    
    }
    
    /**
     * Sends a toast notification to a player.
     * @param player The player to send the message to
     * @param toast The toast notification
     */
    public static void sendToast(Player player, Toast.Toaster toast) {
        Toast.register(toast);
        Advancement advancement = Bukkit.getAdvancement(toast.getNamespacedKey());

        AdvancementProgress awardCriteria = player.getAdvancementProgress(advancement);
        if (!awardCriteria.isDone()) { awardCriteria.getRemainingCriteria().forEach(awardCriteria::awardCriteria); }

        Bukkit.getScheduler().runTaskLater(CoPlugin.getProvidingPlugin(CoPlugin.class),
                () -> {
                    AdvancementProgress revokeCriteria = player.getAdvancementProgress(advancement);
                    if (revokeCriteria.isDone()) { revokeCriteria.getAwardedCriteria().forEach(revokeCriteria::revokeCriteria); }
                }, 20);

        Toast.unregister(toast);
    }
    
    /**
     * Gets a net.minecraft.server class
     * @param className The name of the class
     * @return The class if it exists, null otherwise.
     */
    public static Class<?> getNMSClass(String className) throws ClassNotFoundException {
        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        return Class.forName("net.minecraft.server." + version + "." + className);
    }
    
    public static Class<?> getOBCClass(String className) throws ClassNotFoundException {
        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        return Class.forName("org.bukkit.craftbukkit." + version + "." + className);
    }
}
