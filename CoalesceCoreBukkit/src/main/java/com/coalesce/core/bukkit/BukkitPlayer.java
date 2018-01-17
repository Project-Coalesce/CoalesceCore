package com.coalesce.core.bukkit;

import com.coalesce.core.SenderType;
import com.coalesce.core.plugin.ICoPlugin;
import com.coalesce.core.text.ActionBar;
import com.coalesce.core.text.BossBar;
import com.coalesce.core.text.Text;
import com.coalesce.core.text.Title;
import com.coalesce.core.text.Toast;
import com.coalesce.core.wrappers.CoPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public final class BukkitPlayer implements CoPlayer<Player> {
    
    private final Player player;
    private final CoPlugin plugin;
    
    public BukkitPlayer(Player player, CoPlugin plugin) {
        this.player = player;
        this.plugin = plugin;
    }
    
    @Override
    public void sendActionBar(ActionBar actionBar) {
        //Going to need nms
        //player.sendRawMessage();
    }
    
    @Override
    public void sendTitle(Title title) {
        player.sendTitle(title.getTitle(), title.getSubTitle(), title.fadeInTicks(), title.durationTicks(), title.fadeOutTicks());
    }
    
    @Override
    public void sendToast(Toast toast) {
    
    }
    
    @Override
    public void sendBossBar(BossBar bossBar) {
    
    }
    
    @Override
    public void sendMessage(Text text) {
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
    
    private Object getChatMessageType(byte type) {
        Class<?> chatMessage = getNMSClass("ChatMessageType");
        try {
            return chatMessage.getMethod("a", byte.class).invoke(null, type);
        }
        catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    private Class<?> getNMSClass(String className) {
        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        try {
            return Class.forName("net.minecraft.server." + version + "." + className);
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    private Class<?> getCBClass(String name) {
        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        try {
            return Class.forName("org.bukkit.craftbukkit." + version + "." + name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    @Override
    public void sendMessage(String message) {
        player.sendMessage(message);
    }
    
    @Override
    public String getName() {
        return player.getName();
    }
    
    @Override
    public SenderType getType() {
        return SenderType.PLAYER;
    }
    
    @Override
    public boolean hasPermission(String permission) {
        return player.hasPermission(permission);
    }
    
    @Override
    public Player getBase() {
        return player;
    }
    
    @Override
    public <E extends Player> E as(Class<E> type) {
        return (E)player;
    }
    
    @Override
    public ICoPlugin getPlugin() {
        return plugin;
    }
}
