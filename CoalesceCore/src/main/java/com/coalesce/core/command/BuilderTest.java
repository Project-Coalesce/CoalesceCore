package com.coalesce.core.command;

import com.coalesce.core.Coalesce;
import com.coalesce.core.Color;
import com.coalesce.core.SenderType;
import com.coalesce.core.command.base.CommandContext;
import com.coalesce.core.command.base.ProcessedCommand;
import com.coalesce.core.command.base.TabContext;
import com.coalesce.core.command.defaults.DefaultCommandBuilder;
import com.coalesce.core.plugin.ICoPlugin;
import com.coalesce.core.text.Text;
import org.bukkit.entity.Player;

public final class BuilderTest {

    public BuilderTest(ICoPlugin plugin) {

        ProcessedCommand<CommandContext, TabContext, DefaultCommandBuilder> command = ProcessedCommand.builder(plugin, "test2")
                .permission("core.test1")
                .executor(this::testCommand2)
                .completer(this::testCompletion)
                .minArgs(1)
                .senders(SenderType.PLAYER)
                .usage("/test")
                .description("tests the builder pattern command registration").build();

        plugin.getCommandStore().registerCommand(command);
    }
    
    private void testCompletion(TabContext tabContext) {
        tabContext.completion("hi", "hello");
    }
    
    private void testCommand2(CommandContext context) {
        System.out.println(Color.toConsoleColor('&', context.joinArgs()));
        
        Text text = Text.of("Hello")
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

}
