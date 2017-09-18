package com.coalesce.core.bukkit;

import com.coalesce.core.session.AbstractSession;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PlayerSession extends AbstractSession<Player> {
	
	private final Player player;
	
	public PlayerSession(CoPlugin plugin, String sessionKey, Player player) {
		super(plugin, sessionKey, player);
		this.player = player;
	}
	
	public UUID getUserID() {
		return player.getUniqueId();
	}
	
}
