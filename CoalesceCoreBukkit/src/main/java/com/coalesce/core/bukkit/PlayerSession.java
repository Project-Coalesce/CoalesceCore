package com.coalesce.core.bukkit;

import com.coalesce.core.plugin.ICoPlugin;
import com.coalesce.core.session.AbstractSession;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PlayerSession extends AbstractSession<Player> {
	
	private final Player player;
	
	public PlayerSession(ICoPlugin plugin, String sessionKey, Player player) {
		super(plugin, "players", sessionKey, player);
		this.player = player;
		plugin.getSessionStore().getNamespace("apples", PlayerSession.class).addSession(this);
	}
	
	public UUID getUserID() {
		return player.getUniqueId();
	}
	
}
