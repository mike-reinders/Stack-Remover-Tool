package reinders.mike.StackRemoverTool.CommandTool;

import qowyn.ark.ArkSavegame;
import qowyn.ark.GameObject;
import reinders.mike.StackRemoverTool.Command.Command;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class FindClassesCommand extends Command {

    @Override
    public String getName() {
        return "find-classes";
    }

    @Override
    public String getDescription() {
        return "Finds all classes";
    }

    public String getUsage() {
        return "[--filter=<regex>] [source file]";
    }

    @Override
    public boolean execute() throws Throwable {
        if (this.getParameters().length < 1) {
            this.getCommandManager().dispatch(CommandManager.HELP_COMMAND, new String[] { this.getName() });
            return true;
        }

        Path sourceFilePath = Paths.get(this.getParameters()[0]).toAbsolutePath();
        System.out.println("Loading file '" + sourceFilePath.getFileName() + "'");

        ArkSavegame savegame = new ArkSavegame(sourceFilePath);

        List<String> classNames = new ArrayList<>();
        List<GameObject> objects = savegame.getObjects();

        System.out.println("Looking for classes..");

        String classString;
        for (GameObject obj : objects) {
            classString = obj.getClassString();

            if (!classNames.contains(classString)) {
                classNames.add(classString);
            }
        }

        StringBuilder output = new StringBuilder();

        if (this.isArgument("filter")) {
            System.out.println("Using filter: " + this.getArgument("filter"));
            System.out.println("Filtering classes..");

            Pattern filterPattern = Pattern.compile(this.getArgument("filter"));
            for (String className : classNames) {
                if (filterPattern.matcher(className).matches()) {
                    output.append(System.lineSeparator());
                    output.append(className);
                }
            }
        } else {
            System.out.println("Preparing classes..");

            for (String className : classNames) {
                output.append(System.lineSeparator());
                output.append(className);
            }
        }

        System.out.println("Here are all classes:");
        System.out.println(output);

        return true;
    }

}