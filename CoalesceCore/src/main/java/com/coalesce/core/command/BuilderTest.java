package com.coalesce.core.command;

import com.coalesce.core.Coalesce;
import com.coalesce.core.Color;
import com.coalesce.core.Core;
import com.coalesce.core.SenderType;
import com.coalesce.core.command.base.CommandContext;
import com.coalesce.core.command.base.ProcessedCommand;
import com.coalesce.core.command.base.TabContext;
import com.coalesce.core.command.defaults.DefaultCommandBuilder;
import com.coalesce.core.command.defaults.DefaultProcessedCommand;
import com.coalesce.core.plugin.ICoPlugin;
import com.coalesce.core.text.Text;
import com.coalesce.core.text.Toast;
import org.bukkit.entity.Player;

public final class BuilderTest {

    public BuilderTest(Core plugin) {
    
        DefaultProcessedCommand command = DefaultProcessedCommand.builder(plugin, "test2")
                .permission("core.test1")
                .executor(this::testCommand2)
                .completer(this::testCompletion)
                .minArgs(1)
                .senders(SenderType.PLAYER)
                .usage("/test2")
                .description("tests the builder pattern command registration").build();

        DefaultProcessedCommand command1 = DefaultProcessedCommand.builder(plugin, "test1")
                .permission("core.test2")
                .executor(this::testCommand1)
                .senders(SenderType.PLAYER)
                .usage("/test1")
                .description("Testing the text class and the placeholder replacement for the command context")
                .build();
        
        DefaultProcessedCommand toast = DefaultProcessedCommand.builder(plugin, "toast")
                .permission("core.toast")
                .executor(this::testToast)
                .senders(SenderType.PLAYER)
                .usage("/toast")
                .description("tests the toast builder").build();
        
        plugin.getCommandStore().registerCommands(command, toast, command1);
    }
    
    private void testCompletion(TabContext tabContext) {
        tabContext.completion("hi", "hello");
    }
    
    private void testCommand1(CommandContext context) {
        
        Text.TextSection text = Text.of("This is a test for {0}")
                .setColor(Color.PURPLE)
                .setBold(true)
                .setUnderlined(true)
                .clickEvent(e -> {
                    e.action(Text.ClickAction.SUGGEST_COMMAND);
                    e.click("{1}");
                })
                .hoverEvent(e -> {
                    e.action(Text.HoverAction.SHOW_TEXT);
                    e.hover(s -> s.setText("{0} and {1} and {2}"));
                });
        
        context.pluginMessage(text, context.getSender().getName(), context.getSender().getType(), context.getPlugin().getDisplayName());
    }
    
    private void testCommand2(CommandContext context) {
        System.out.println(Color.toConsoleColor('&', context.joinArgs()));
        
        Text.TextSection text = Text.of("Hello")
                .setColor(Color.BLUE)
                .setBold(true)
                .setItalics(true)
                .clickEvent((e) -> {
                    e.action(Text.ClickAction.SUGGEST_COMMAND);
                    e.click("click");
                })
                .hoverEvent((e) -> {
                    e.action(Text.HoverAction.SHOW_TEXT);
                    e.hover((s) -> s.setText("hover"));
                })
                .shiftClickEvent("insert");
        
        text.append((s) -> {
            s.setText(" im poo")
                    .setBold(true)
                    .setUnderlined(true)
                    .setColor(Color.DARK_GREEN)
                    .hoverEvent((e) -> {
                        e.action(Text.HoverAction.SHOW_TEXT);
                        e.hover((h) -> h.setText("there").setColor(Color.PURPLE));
                    })
                    .clickEvent((e) -> {
                        e.action(Text.ClickAction.RUN_COMMAND);
                        e.click("/help");
                    });
        });
        Coalesce.sendMessage(context.getSender().as(Player.class), text);
    }

    private void testToast(CommandContext context) {
        Coalesce.sendToast(context.getSender().as(Player.class),
                Toast.of("Test toast")
                        .setText(Text.of("Congratulations! It works :)").setColor(Color.GOLD))
                        .setIcon("minecraft:red_flower"));
    }
}
