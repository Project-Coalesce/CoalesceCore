package com.coalesce.core.scoreboard;

import com.coalesce.core.Color;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.HashMap;
import java.util.Map;

public class StaticScoreboard implements CoScoreboard<String> {
    
    private String title;
    private Objective objective;
    private final Scoreboard scoreboard;
    private final Map<Integer, String> lines;
    
    public StaticScoreboard() {
        this.lines = new HashMap<>();
        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
    }
    
    @Override
    public Map<Integer, String> getLines() {
        return lines;
    }
    
    @Override
    public Objective getObjective() {
        return objective;
    }
    
    @Override
    public Scoreboard getScoreboard() {
        return scoreboard;
    }
    
    @Override
    public String blankLine(int line) {
        String base = Color.RESET.toString();
        for (int i = 0; i <  line; i++) {
            base = base.concat(Color.RESET.toString());
        }
        return base;
    }
    
    @Override
    public void setTitle(String title) {
        this.title = title;
    }
    
    @Override
    public void send(Player player) {
        
        if (objective != null) {
            objective.unregister();
        }
        //Resets the objective
        objective = scoreboard.registerNewObjective(title, "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(title);
        
        //Updates the scoreboard
        int i = 0;
        while (i < lines.size()) {
            objective.getScore(lines.get(i)).setScore(-i+15);
            i++;
        }
        
        player.setScoreboard(scoreboard);
    }
    
}
