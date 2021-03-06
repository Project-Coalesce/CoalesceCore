package com.coalesce.core.gui;

import com.coalesce.core.plugin.ICoPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Represents an inventory that can change between players.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public abstract class PlayerGui implements Gui<Function<Player, ItemStack>, PlayerGui>, Listener {
    
    /**
     * The plugin
     */
    protected final ICoPlugin plugin;
    
    /**
     * The size of the inventory
     */
    protected final int size;
    /**
     * The function to apply to get the title
     */
    protected final Function<Player, String> title;
    /**
     * The functions to apply to get each item
     */
    private final Function<Player, ItemStack>[] items;
    
    /**
     * The consumers to run when each item is clicked
     */
    private final Consumer<InventoryClickEvent>[] listeners;
    
    /**
     * Map of current inventories, entries are removed on close
     */
    protected final Map<UUID, Inventory> inventories;
    
    @SuppressWarnings( "unchecked" )
    public PlayerGui(ICoPlugin plugin, int size, Function<Player, String> title) {
        this.plugin = plugin;
        this.size = size;
        this.title = title;
        this.items = new Function[size];
        this.listeners = new Consumer[size];
        this.inventories = new HashMap<>();
        
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    
    public PlayerGui(ICoPlugin plugin, int size, String title) {
        this(plugin, size, p -> title);
    }

    @Override
    public PlayerGui addItem(Function<Player, ItemStack> item, Consumer<InventoryClickEvent> onClick) {
        for (int i = 0; i < items.length; i++) {
            if (items[i] == null) {
                this.items[i] = item;
                this.listeners[i] = onClick;
                break;
            }
        }
        return this;
    }

    @Override
    public PlayerGui setItem(int index, Function<Player, ItemStack> item, Consumer<InventoryClickEvent> onClick) {
        this.items[index] = item;
        this.listeners[index] = onClick;
        return this;
    }

    @Override
    public PlayerGui removeItem(int index) {

        this.items[index] = null;
        this.listeners[index] = null;
        return this;
    }

    public PlayerGui removeItems(int startIndex, int countToRemove) {

        for (int i = startIndex; i < startIndex + countToRemove; i++) {
            if (i >= size) {
                break;
            }

            this.items[i] = null;
            this.listeners[i] = null;
        }
        return this;
    }
    
    public void update(Player player) {
        Inventory inventory = inventories.computeIfAbsent(player.getUniqueId(), uuid -> this.getNewInventory(player));
        buildInventory(player, inventory);
        player.updateInventory();
    }

    @Override
    public void open(Player player) {
        Inventory inventory = inventories.computeIfAbsent(player.getUniqueId(), uuid -> this.getNewInventory(player));
        player.openInventory(inventory);
    }
    
    
    private Inventory getNewInventory(Player player) {
        Inventory inventory = Bukkit.createInventory(null, this.size, this.title.apply(player));
        buildInventory(player, inventory);
        return inventory;
    }
    
    private void buildInventory(Player player, Inventory inventory) {
        inventory.clear();
        for (int i = 0; i < items.length; i++) {
            Function<Player, ItemStack> function = items[i];
            if (function != null) {
                ItemStack item = function.apply(player);
                inventory.setItem(i, item);
            }
        }
    }

    @Override
    public void clear() {
        Bukkit.getOnlinePlayers().stream().filter(p -> inventories.containsKey(p.getUniqueId())).forEach(HumanEntity::closeInventory);
        inventories.clear();
    }

    /**
     * Unregisters all listeners of the GUI. This is done to get rid of useless {@link Listener}s that are no longer used
     */
    public void destroy() {

        InventoryCloseEvent.getHandlerList().unregister(this);
        InventoryDragEvent.getHandlerList().unregister(this);
        InventoryCloseEvent.getHandlerList().unregister(this);

        inventories.clear();
    }
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player) || event.getClickedInventory() == null) {
            return;
        }
        Player player = (Player)event.getWhoClicked();
        Inventory inventory = inventories.get(player.getUniqueId());
        if (inventory == null) {
            return;
        }
        
        if (inventory.equals(event.getClickedInventory())) {
            event.setCancelled(true);
            Consumer<InventoryClickEvent> onClick = listeners[event.getSlot()];
            if (onClick != null) {
                try {
                    onClick.accept(event);
                }
                catch (Exception e) {
                    throw new RuntimeException("Failed to handle inventory click event", e);
                }
            }
        } else if (inventories.containsValue(event.getView().getTopInventory())) {
            event.setResult(Result.DENY);
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (inventories.containsValue(event.getInventory()) && event.getWhoClicked() instanceof Player) {
            event.setResult(Result.DENY);
            event.setCancelled(true);
            ((Player)event.getWhoClicked()).updateInventory();
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        this.onClose(event);
    }
    
    public void onClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player) || inventories.values().stream().noneMatch(inv -> event.getInventory().equals(inv))) {
            return;
        }
        
        inventories.remove(event.getPlayer().getUniqueId());
    }
} 