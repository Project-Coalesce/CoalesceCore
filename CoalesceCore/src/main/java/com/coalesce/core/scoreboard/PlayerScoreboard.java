package com.coalesce.core.scoreboard;

import com.coalesce.core.Color;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class PlayerScoreboard implements CoScoreboard<Function<Player, String>> {
    
    private Objective objective;
    private final Scoreboard scoreboard;
    private Function<Player, String> title;
    private final Map<Integer, Function<Player, String>> lines;
    
    public PlayerScoreboard() {
        this.lines = new HashMap<>();
        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
    }
    
    @Override
    public Map<Integer, Function<Player, String>> getLines() {
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
    public Function<Player, String> blankLine(int line) {
        String base = Color.RESET.toString();
        for (int i = 0; i <  line; i++) {
            base = base.concat(Color.RESET.toString());
        }
        String finalBase = base;
        return player -> finalBase;
    }
    
    @Override
    public void setTitle(Function<Player, String> title) {
        this.title = title;
    }
    
    @Override
    public void send(Player player) {
        
        if (objective != null) {
            objective.unregister();
        }
        //Resets the objective
        objective = scoreboard.registerNewObjective(title.apply(player), "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(title.apply(player));
        
        //Updates the scoreboard
        int i = 0;
        while (i < lines.size()) {
            objective.getScore(lines.get(i).apply(player)).setScore(-i+15);
            i++;
        }
        
        player.setScoreboard(scoreboard);
    }
}
