package com.coalesce.core.sponge;

import com.coalesce.core.plugin.ICoPlugin;
import com.coalesce.core.session.AbstractSession;
import org.spongepowered.api.entity.living.player.Player;

import java.util.UUID;

public class PlayerSession extends AbstractSession<Player> {
	
	private final Player player;
	
	public PlayerSession(ICoPlugin sessionOwner, String sessionKey, Player player) {
		super(sessionOwner, sessionKey, player);
		this.player = player;
	}
	
	public UUID getUserId() {
		return player.getUniqueId();
	}
}
