package reinders.mike.StackRemoverTool.CommandTool;

import reinders.mike.StackRemoverTool.Command.Command;

public class CommandManager extends reinders.mike.StackRemoverTool.Command.CommandManager {

    public static final Command HELP_COMMAND = new HelpCommand();

    public CommandManager() {
        this.register(CommandManager.HELP_COMMAND);
    }

}