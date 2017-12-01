package com.coalesce.core.sponge;

import com.coalesce.core.wrappers.CoSender;
import com.coalesce.core.SenderType;
import com.coalesce.core.plugin.ICoPlugin;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.source.CommandBlockSource;
import org.spongepowered.api.command.source.ConsoleSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

public final class SpongeSender implements CoSender<CommandSource> {
	
	private final CommandSource sender;
	private final ICoPlugin plugin;
	
	public
	SpongeSender(CommandSource sender, ICoPlugin plugin) {
		this.sender = sender;
		this.plugin = plugin;
	}
	
	@Override
	public void sendMessage(String message) {
		sender.sendMessage(Text.of(message));
	}
	
	@Override
	public String getName() {
		return sender.getName();
	}
	
	@Override
	public SenderType getType() {
		if (sender instanceof ConsoleSource) return SenderType.CONSOLE;
		if (sender instanceof CommandBlockSource) return SenderType.BLOCK;
		if (sender instanceof Player) return SenderType.PLAYER;
		return SenderType.OTHER;
	}
	
	@Override
	public boolean hasPermission(String permission) {
		return sender.hasPermission(permission);
	}
	
	@Override
	public CommandSource getBase() {
		return sender;
	}
	
	@Override
	public <E extends CommandSource> E as(Class<E> type) {
		return (E)sender;
	}
	
	@Override
	public ICoPlugin getPlugin() {
		return plugin;
	}
}
