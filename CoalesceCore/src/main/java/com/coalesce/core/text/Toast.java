package com.coalesce.core.text;

import com.coalesce.core.CoPlugin;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

public class Toast {

    private static final Plugin INSTANCE = CoPlugin.getProvidingPlugin(CoPlugin.class);
    private static final String FRAME = "goal"; // ehh

    /**
     * Creates a new Toast
     * @param text - Predetermined text for the toast
     * @return a new toaster!
     */
    public static Toaster of(String text) {
        return new Toaster().setText(Text.of(text));
    }

    /**
     * Registers an advancement in Bukkit's super safe API
     * @param toaster - Toaster where we'll get our properties
     * @throws IllegalArgumentException - If there's an advancement with the same name
     */
    public static void register(Toaster toaster) throws IllegalArgumentException {
        Bukkit.getUnsafe().loadAdvancement(toaster.getNamespacedKey(), toaster.toString());
    }

    /**
     * Unregisters an already used toast
     * @param toaster - Toast to be removed
     */
    public static void unregister(Toaster toaster) {
        Bukkit.getUnsafe().removeAdvancement(toaster.getNamespacedKey());
    }

    /**
     * Represents the toaster who provides all properties for the toast
     */
    public static class Toaster {
        private String title;
        private String text;
        private String icon;

        // We don't want this to change, do we
        private final boolean announce = false, toast = true;

        private final NamespacedKey namespacedKey;

        private Toaster() {
            // It requires an unique name, so fuck it
            namespacedKey = new NamespacedKey(INSTANCE, UUID.randomUUID().toString());
        }

        /**
         * Sets the title of the toast
         * @param title - TextSelection formatted for this title.
         * @return this builder
         */
        public Toaster setTitle(Text.TextSection title) {
            this.title = title.toString();
            return this;
        }

        /**
         * Sets the text for this toast
         * @param text - TextSelection formatted for this text description
         * @return this builder
         */
        public Toaster setText(Text.TextSection text) {
            this.text = text.toString();
            return this;
        }

        /**
         * Sets the toast icon
         * @param icon - Minecraft's item text type, consult <url>http://minecraft-ids.grahamedgecombe.com/items.json</url>
         *             for more information.
         * @return this builder
         */
        // TODO: Find a workaround for this (making enums, parsing from json?)
        public Toaster setIcon(String icon) {
            this.icon = icon;
            return this;
        }

        /**
         * Get the NamespacedKey required to register the toast
         * @return - the NamespacedKey
         */
        public NamespacedKey getNamespacedKey() {
            return namespacedKey;
        }

        /**
         * JsonObject toast builder
         * @return the JsonObject to later send as reward
         */
        public JsonObject getJson() {
            JsonObject icon = new JsonObject();
            icon.addProperty("item", this.icon);

            JsonObject json = new JsonObject();
            json.add("icon", icon);
            json.addProperty("title", this.title);
            json.addProperty("description", this.text);
            json.addProperty("background", "minecraft:textures/gui/advancements/backgrounds/adventure.png");
            json.addProperty("frame", FRAME);
            json.addProperty("announce_to_chat", this.announce);
            json.addProperty("show_toast", this.toast);
            json.addProperty("hidden", true);
            JsonObject criteria = new JsonObject();
            JsonObject trigger = new JsonObject();

            trigger.addProperty("trigger", "minecraft:impossible");
            criteria.add("impossible", trigger);

            json.add("criteria", criteria);
            json.add("display", json);
            return json;
        }

        /**
         * Turns this Toaster into an String
         * @return - Json string
         */
        @Override
        public String toString() {
            return getJson().toString();
        }
    }
}
