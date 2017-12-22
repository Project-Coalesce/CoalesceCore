package com.coalesce.core.sponge;

import com.coalesce.core.command.base.CommandInfo;
import com.coalesce.core.plugin.ICoPlugin;
import org.spongepowered.api.command.CommandCallable;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public final class CommandRegister implements CommandCallable {

    private final CommandInfo command;
    private final ICoPlugin plugin;

    public CommandRegister(CommandInfo info, ICoPlugin plugin) {
        this.plugin = plugin;
        this.command = info;
    }

    @Override
    public CommandResult process(CommandSource source, String arguments) throws CommandException {
        command.run(new SpongeSender(source, plugin), (arguments.isEmpty() ? new String[0] : arguments.split(" ")));
        return CommandResult.success();
    }

    @Override
    public List<String> getSuggestions(CommandSource source, String arguments,
            @Nullable
                    Location<World> targetPosition) throws CommandException {
        return command.complete(new SpongeSender(source, plugin), arguments.split(" "));
    }

    @Override
    public boolean testPermission(CommandSource source) {
        if (command.getPermission() == null) return true;
        for (String permission : command.getPermission()) {
            if (source.hasPermission(permission)) return true;
        }
        return false;
    }

    @Override
    public Optional<Text> getShortDescription(CommandSource source) {
        return Optional.of(Text.of(command.getDesc()));
    }

    @Override
    public Optional<Text> getHelp(CommandSource source) {
        return Optional.of(Text.of(command.getDesc()));
    }

    @Override
    public Text getUsage(CommandSource source) {
        return Text.of(command.getUsage());
    }
}
