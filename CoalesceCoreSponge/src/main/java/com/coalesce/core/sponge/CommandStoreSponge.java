package com.coalesce.core.sponge;

import com.coalesce.core.command.base.CommandInfo;
import com.coalesce.core.command.base.CommandStore;
import com.coalesce.core.plugin.ICoPlugin;
import org.spongepowered.api.Sponge;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class CommandStoreSponge extends CommandStore {
	
	private final ICoPlugin plugin;
	
	public CommandStoreSponge(ICoPlugin plugin) {
		super(plugin);
		this.plugin = plugin;
	}
	
	@Override
	protected void registerCommand(CommandInfo info) {
		List<String> aliases = new ArrayList<>();
		aliases.addAll(info.getAliases());
		aliases.add(info.getName());
		
		Sponge.getCommandManager().register(plugin, new CommandRegister(info, plugin), aliases);
	}
	
	@Override
	public boolean isRegistered(String commandName) {
		return Sponge.getCommandManager().get(commandName).isPresent();
	}
	
	@Override
	protected void registerObjects() {
		register();
	}
	
	
}
