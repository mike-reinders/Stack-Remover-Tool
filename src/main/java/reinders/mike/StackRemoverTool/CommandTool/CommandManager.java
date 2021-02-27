package reinders.mike.StackRemoverTool.CommandTool;

import reinders.mike.StackRemoverTool.Command.Command;

public class CommandManager extends reinders.mike.StackRemoverTool.Command.CommandManager {

    public static final Command FIND_CLASSES_COMMAND = new FindClassesCommand();
    public static final Command FIND_REFERENCES_COMMAND = new FindReferencesCommand();
    public static final Command MAP_CLASSES_COMMAND = new MapClassesCommand();
    public static final Command MAP_REFERENCES_COMMAND = new MapReferencesCommand();
    public static final Command HELP_COMMAND = new HelpCommand();
    public static final Command DUMP_MOD_COMMAND = new DumpModCommand();

    public CommandManager() {
        this.register(CommandManager.FIND_CLASSES_COMMAND);
        this.register(CommandManager.FIND_REFERENCES_COMMAND);
        this.register(CommandManager.MAP_CLASSES_COMMAND);
        this.register(CommandManager.MAP_REFERENCES_COMMAND);
        this.register(CommandManager.DUMP_MOD_COMMAND);
        this.register(CommandManager.HELP_COMMAND);
    }

}