package com.coalesce.core.text;

import com.coalesce.core.Color;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@SuppressWarnings({"WeakerAccess", "unused"})
public abstract class Text {
    
    private static TextSection parent;
    private List<TextSection> extra;
    private boolean isParent = false;
    
    /**
     * Creates a new Text.
     * @param text The beginning text
     * @return A new Text Section. This will clear all existing text sections.
     */
    public static TextSection of(String text) {
        TextSection section = new TextSection(true).setText(text);
        section.setParent(true);
        return parent = section;
    }
    
    /**
     * Adds a text section to the current Text
     * @param text The text to append to this current section
     * @return The newly created text section
     */
    public final TextSection append(String text) {
        TextSection section = new TextSection(true).setText(text);
        getExtra().add(section);
        return section;
    }
    
    public final void append(Consumer<TextSection> consumer) {
        TextSection section = new TextSection(true);
        consumer.accept(section);
        getExtra().add(section);
    }
    
    public List<TextSection> getExtra() {
        if (isParent()) {
            if (extra == null) return extra = new ArrayList<>();
            return extra;
        }
        else return parent.getExtra();
    }
    
    final void setParent(boolean parent) {
        this.isParent = parent;
    }
    
    public final boolean isParent() {
        return isParent;
    }
    
    /**
     * Represents a section of text
     */
    public static class TextSection extends Text {
        
        private String text = "";
        private HoverEvent hoverEvent;
        private ClickEvent clickEvent;
        private Color color = Color.RESET;
        private boolean bold = false;
        private boolean italics = false;
        private boolean underline = false;
        private boolean obfuscated = false;
        private boolean canHaveEvents = true;
        private boolean strikethrough = false;
        
        private TextSection(boolean events) {
            this.canHaveEvents = events;
        }
    
        /**
         * Set the text of this TextSection
         * @param text The text
         * @return This TextSection
         */
        public TextSection setText(String text) {
            this.text = text;
            return this;
        }
        
        /**
         * Set the color of the current text.
         * @param color The color.
         *
         *              <p>
         *              Any can be set. Any formatting such as bold or italics must be set with the appropriate methods.
         *              </p>
         */
        public TextSection setColor(Color color) {
            if (21 > color.ordinal() && color.ordinal() > 15) {
                return this;
            }
            this.color = color;
            return this;
        }
    
        /**
         * Sets this Section to bold
         * @param bold True to set bold, false otherwise
         * @return This TextSection
         */
        public TextSection setBold(boolean bold) {
            this.bold = bold;
            return this;
        }
        
        /**
         * Sets this section to Italics
         * @param italics True to set italics, false otherwise
         * @return This TextSection
         */
        public TextSection setItalics(boolean italics) {
            this.italics = italics;
            return this;
        }
    
        /**
         * Sets this section to underlined
         * @param underline True to set underlined, false otherwise
         * @return This TextSection
         */
        public TextSection setUnderlined(boolean underline) {
            this.underline = underline;
            return this;
        }
    
        /**
         * Sets this section to obfuscated
         * @param obfuscated True to set obfuscated, false otherwise
         * @return This TextSection
         */
        public TextSection setObfuscated(boolean obfuscated) {
            this.obfuscated = obfuscated;
            return this;
        }
    
        /**
         * Sets this section to Strikethroughed
         * @param strikethrough True to set strikethrough, false otherwse
         * @return This TextSection
         */
        public TextSection setStrikethrough(boolean strikethrough) {
            this.strikethrough = strikethrough;
            return this;
        }
    
        /**
         * Adds a hover event to this text section
         * @param hoverEvent The hover event
         * @return This TextSection
         */
        public TextSection hoverEvent(Consumer<HoverEvent> hoverEvent) {
            if (!canHaveEvents) return this;
            this.hoverEvent = new HoverEvent();
            hoverEvent.accept(this.hoverEvent);
            return this;
        }
        
        public TextSection hoverEvent(HoverEvent hoverEvent) {
            if (!canHaveEvents) return this;
            this.hoverEvent = hoverEvent;
            return this;
        }
        
        public TextSection clickEvent(Consumer<ClickEvent> clickEvent) {
            if (!canHaveEvents) return this;
            this.clickEvent = new ClickEvent();
            clickEvent.accept(this.clickEvent);
            return this;
        }
        
        public TextSection clickEvent(ClickEvent clickEvent) {
            if (!canHaveEvents) return this;
            this.clickEvent = clickEvent;
            return this;
        }
        
        public JsonObject getJson() {
    
            JsonObject hover = null;
            if (hoverEvent != null && canHaveEvents) {
                hover = new JsonObject();
                List<JsonObject> val = new ArrayList<>();
                val.add(hoverEvent.getHover().getJson());
                hover.addProperty("action", hoverEvent.getAction().toString().toLowerCase());
                hover.add("value", new GsonBuilder().setPrettyPrinting().create().toJsonTree(val));
            }
    
            JsonObject click = null;
            if (clickEvent != null && canHaveEvents) {
                click = new JsonObject();
                click.addProperty("action", clickEvent.getAction().toString().toLowerCase());
                click.addProperty("value", clickEvent.getClick());
            }
    
            JsonObject json = new JsonObject();
            json.addProperty("text", text);
            if (bold) json.addProperty("bold", true);
            if (italics) json.addProperty("italic", true);
            if (underline) json.addProperty("underlined", true);
            if (obfuscated) json.addProperty("obfuscated", true);
            if (strikethrough) json.addProperty("strikethrough", true);
            if (hover != null) {
                json.add("hoverEvent", hover);
            }
            if (click != null) {
                json.add("clickEvent", click);
            }
            return json;
        }
    
        @Override
        public String toString() {
            return getJson().toString();
        }
    }
    
    /**
     * Represents a hover event in chat
     */
    public class HoverEvent {
        
        private HoverAction action = HoverAction.SHOW_TEXT;
        private TextSection text = new TextSection(false);
        
        public final void action(HoverAction action) {
            this.action = action;
        }
    
        public final HoverAction getAction() {
            return action;
        }
        
        public final void hover(Consumer<TextSection> text) {
            text.accept(this.text);
        }
        
        public final TextSection getHover() {
            return text;
        }
    }
    
    /**
     * The {@link HoverEvent} actions
     */
    public enum HoverAction {
        /**
         * Display text when the user hovers over the message
         */
        SHOW_TEXT,
        /**
         * Display an item when the user hovers over the message
         */
        SHOW_ITEM,
        /**
         * Display an entity when the user hovers over the message
         */
        SHOW_ENTITY
        
    }
    
    /**
     * Represents a click event in chat
     */
    public class ClickEvent {
    
        private ClickAction action = ClickAction.SUGGEST_COMMAND;
        private String click;
        
        public void action(ClickAction action) {
            this.action = action;
        }
    
        public ClickAction getAction() {
            return action;
        }
        
        public void click(String click) {
            this.click = click;
        }
    
        public String getClick() {
            return click;
        }
    }
    
    /**
     * The {@link ClickEvent} actions
     */
    public enum ClickAction {
    
        /**
         * Open a URL for a user when clicked
         */
        OPEN_URL,
        /**
         * Run a command for a user when clicked
         */
        RUN_COMMAND,
        /**
         * Suggest a command for a user when clicked
         */
        SUGGEST_COMMAND,
        /**
         * Change the page for a user when clicked
         */
        CHANGE_PAGE
    
    }
    
    /**
     * Turns this text object into the proper JSON format
     * @return Json string
     */
    @Override
    public String toString() {
        JsonArray extra = new JsonArray();
        for (TextSection section : parent.getExtra()) {
            extra.add(section.toString());
        }
        parent.getJson().add("extra", extra);
        return parent.toString();
    }
}
