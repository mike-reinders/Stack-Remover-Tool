package reinders.mike.StackRemoverTool.CommandTool;

import qowyn.ark.ArkSavegame;
import qowyn.ark.GameObject;
import reinders.mike.StackRemoverTool.ClassMapping.Mapping;
import reinders.mike.StackRemoverTool.ClassMapping.MappingInterface;
import reinders.mike.StackRemoverTool.Command.Command;
import reinders.mike.StackRemoverTool.Util.Pad;
import reinders.mike.StackRemoverTool.Util.StringC;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class MapClassesCommand extends Command {

    @Override
    public String getName() {
        return "map-classes";
    }

    @Override
    public String getDescription() {
        return "Replaces all classes by given mapping";
    }

    public String getUsage() {
        return "[--mapping=<name>] [--debug] [source file] [target file]" + System.lineSeparator()
                + "Available Mappings:" + System.lineSeparator()
                + "  - UltraStacks";
    }

    @Override
    public boolean execute() throws Throwable {
        if (this.getParameters().length < 2 || !this.isArgument("mapping")) {
            this.getCommandManager().dispatch(CommandManager.HELP_COMMAND, new String[] { this.getName() });
            return true;
        }

        String mappingName = this.getArgument("mapping");
        MappingInterface mapping = Mapping.getMapping(mappingName);

        if (mapping == null) {
            System.out.println("Couldn't find mapping '" + mappingName + "'");
            return true;
        } else {
            System.out.println("Loaded mapping '" + mapping.getName() + "'");
        }

        Path sourceFilePath = Paths.get(this.getParameters()[0]).toAbsolutePath();
        Path targetFilePath = Paths.get(this.getParameters()[1]).toAbsolutePath();
        System.out.println("Loading source file '" + sourceFilePath.getFileName() + "'");

        ArkSavegame savegame = new ArkSavegame(sourceFilePath);

        List<GameObject> objects = savegame.getObjects();

        System.out.println("Mapping classes..");

        boolean debug = this.isArgument("debug");
        StringBuilder strBuilder = new StringBuilder();

        int idWidth = String.valueOf(objects.size()).length() + 2;

        String className;
        String origin;
        for (GameObject obj : objects) {
            className = obj.getClassString();
            origin = mapping.getOrigin(className);

            if (origin != null) {
                obj.setClassString(origin);
                if (debug) {
                    strBuilder.append(System.lineSeparator());
                    strBuilder.append("# ");
                    strBuilder.append(StringC.pad(Pad.RIGHT, String.valueOf(obj.getId()), idWidth));
                    strBuilder.append(StringC.pad(Pad.RIGHT, className, 60));
                    strBuilder.append(" => ");
                    strBuilder.append(origin);
                }
            }
        }

        System.out.println(strBuilder);

        System.out.println("Saving target file '" + targetFilePath.getFileName() + "'");
        savegame.writeBinary(targetFilePath);

        return true;
    }

}