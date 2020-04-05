package reinders.mike.StackRemoverTool.CommandTool;

import reinders.mike.StackRemoverTool.Command.Command;

public class CommandManager extends reinders.mike.StackRemoverTool.Command.CommandManager {

    public static final Command FIND_CLASSES_COMMAND = new FindClassesCommand();
    public static final Command MAP_CLASSES_COMMAND = new MapClassesCommand();
    public static final Command HELP_COMMAND = new HelpCommand();

    public CommandManager() {
        this.register(CommandManager.FIND_CLASSES_COMMAND);
        this.register(CommandManager.MAP_CLASSES_COMMAND);
        this.register(CommandManager.HELP_COMMAND);
    }

}