package com.coalesce.core.scoreboard;

import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.Collection;
import java.util.Map;

/**
 * Provides a general contract for scoreboard display interfaces consisting of String data mapped to Integer scores
 *
 * @param <T> The type of data to be displayed in the Scoreboard.
 */
@SuppressWarnings("unused")
public interface CoScoreboard<T> {

    //Max number of entries that are allowed in a CoScoreboard
    int MAX_LINES = 15;
    
    /**
     * Gets all the lines in this scoreboard
     * @return All the lines
     */
    Map<Integer, T> getLines();
    
    /**
     * Gets the objective currently being used on the scoreboard
     * @return The current objective
     */
    Objective getObjective();
    
    /**
     * Gets this current scoreboard
     * @return The scoreboard
     */
    Scoreboard getScoreboard();
    
    /**
     * INTERNAL USE ONLY
     */
    T blankLine(int line);
    
    /**
     * Sets the title of this scoreboard
     *
     * @param title The scoreboard title
     */
    void setTitle(T title);
    
    /**
     * Sends the {@link Scoreboard} representation of the CoScoreboard to the player
     *
     * @param player The player to send the {@link Scoreboard} to.
     */
    void send(Player player);
    
    /**
     * Sends the {@link Scoreboard} representation of the CoScoreboard to the provided Collection of players
     *
     * @param players The players to send the {@link Scoreboard} to.
     */
    default void send(Collection<Player> players) {
        players.forEach(this::send);
    }
    
    /**
     * This sets the specified line to a blank line.
     * @param line The line to set blank
     */
    default void setLine(int line) {
        setLine(line, blankLine(line));
    }
    
    /**
     * Will add another line of text to the specified line. If the line already exists, this new text will replace <p>
     * it, if it doesn't exist it will be added and any lines in between will be filled with blank lines.
     *
     * @param text The text to add
     * @param line The line to add the text to
     */
    default void setLine(int line, T text) {
        if (MAX_LINES <= line) return;
        if (line > getLines().size() - 1) {
            int i = getLines().size();
            while (i < line) {
                getLines().put(i, blankLine(i));
                i++;
            }
            if (blankLine(line).equals(text)) getLines().put(i, blankLine(line));
            else getLines().put(i, text);
        }
    }
    
    /**
     * Swaps 2 lines.
     * @param line Line being swapped
     * @param newLine Line the first line is being swapped with
     */
    default void swapLines(int line, int newLine) {
        if (MAX_LINES <= line || MAX_LINES <= newLine) return;
        if (getLines().size()-1 >= line && getLines().size()-1 >= newLine) {
            T line1 = getLines().get(line);
            T line2 = getLines().get(newLine);
            getLines().put(newLine, line1);
            getLines().put(line, line2);
        }
    }
    
    /**
     * Replaces a line with new text
     * @param line The line to replace
     * @param text The text to put on the line.
     */
    default void replaceLine(int line, T text) {
        if (MAX_LINES <= line) return;
        if (getLines().size()-1 >= line) {
            getLines().put(line, text);
        }
    }
    
    /**
     * Removes all the lines in the scoreboard
     */
    default void removeAll() {
        getLines().clear();
    }
    
    /**
     * Remove the line specified and replace it with a blank line.
     * @param line The line to remove.
     */
    default void removeLine(int line) {
        removeLine(line, false);
    }
    
    /**
     * Remove the line specified.
     * @param line The line to remove
     * @param moveOthers If true, when this line is removed, the lines below it will be moved up accordingly. If false, this line will become a blank line.
     */
    default void removeLine(int line, boolean moveOthers) {
        if (MAX_LINES <= line) return;
        if (getLines().size()-1 >= line) {
            if (moveOthers) getLines().remove(line);
            else getLines().put(line, blankLine(line));
        }
    }
    
    /**
     * Will add another line of text to the specified line. If the line already has text, the line will not be added.<p>
     * This will also add blank lines between the last line and this line if the last line is more than one line away
     * @param text The text to add
     * @param line The line to add the text to (0-15, 0 being the top of the scoreboard and 15 being the bottom)
     */
    default void addLine(int line, T text) {
        if (MAX_LINES <= line) return;
        if (getLines().size()-1 < line) {
            setLine(line, text);
        }
    }
    
    /**
     * Will add another line of text to the next line available in the scoreboard
     * @param text The text to add on the next line.
     */
    default void addLine(T text) {
        if (MAX_LINES <= getLines().size()) return;
        getLines().put(getLines().size(), text);
    }
    
    /**
     * Adds an empty line to the next available line.
     */
    default void addLine() {
        if (MAX_LINES <= getLines().size()) return;
        addLine(blankLine(getLines().size()));
    }

}
