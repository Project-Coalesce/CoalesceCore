package com.coalesce.core.command;

import com.coalesce.core.Coalesce;
import com.coalesce.core.Color;
import com.coalesce.core.SenderType;
import com.coalesce.core.command.annotation.Alias;
import com.coalesce.core.command.annotation.Command;
import com.coalesce.core.command.annotation.Completion;
import com.coalesce.core.command.annotation.Permission;
import com.coalesce.core.command.annotation.Sender;
import com.coalesce.core.command.base.CommandContext;
import com.coalesce.core.command.base.TabContext;
import com.coalesce.core.config.YmlConfig;
import com.coalesce.core.text.Text;

public final class TestCommand {

    @Alias( {"tst", "ts"} )
    @Permission( "core.test" )
    @Sender( {SenderType.PLAYER, SenderType.CONSOLE} )
    @Command( name = "test",
              desc = "A test command",
              usage = "/test",
              min = 0 )
    public void testCommand(CommandContext context) {
        System.out.println(Color.toConsoleColor('&', context.joinArgs()));
        
        Text text = Text.of("Hello")
                .setColor(Color.BLUE)
                .setBold(true)
                .setItalics(true)
                /*.clickEvent((e) -> {
                    e.action(Text.ClickAction.SUGGEST_COMMAND);
                    e.click((s) -> s.setText("click"));
                })*/
                .hoverEvent((e) -> {
                    e.action(Text.HoverAction.SHOW_TEXT);
                    e.hover((s) -> s.setText("hover"));
                });
    
        System.out.println(context.getSender().getName());
        Coalesce.getCoPlayer(context.getSender().getName()).sendMessage(text);
    }

    @Completion( "test" )
    public void testCompletion(TabContext context) {
        context.completionAt(0, "hello");
    }

}
