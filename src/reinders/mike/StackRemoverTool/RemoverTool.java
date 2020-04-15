package reinders.mike.StackRemoverTool;

import reinders.mike.StackRemoverTool.CommandTool.CommandManager;
import reinders.mike.StackRemoverTool.Util.ThrowableC;

public final class RemoverTool {

    public static final String MERGE_TOOL_VERSION = "1.1";

    private static reinders.mike.StackRemoverTool.Command.CommandManager commandManager = new CommandManager();

    public static reinders.mike.StackRemoverTool.Command.CommandManager getCommandManager() {
        return RemoverTool.commandManager;
    }

    public static void main(String[] args) {
        try {
            System.out.println("Stack Remover Tool v" + RemoverTool.MERGE_TOOL_VERSION);
            if (args.length == 0 || !RemoverTool.commandManager.dispatch(args)) {
                RemoverTool.commandManager.dispatch(CommandManager.HELP_COMMAND);
            }
        } catch (Throwable throwable) {
            System.out.println(ThrowableC.toString(throwable));
        }
    }

}