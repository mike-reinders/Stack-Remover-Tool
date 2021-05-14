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
        StringBuilder usage = new StringBuilder("[--match-alert=<regex>]  [--from-mapping=<name>] [--to-mapping=<name>] [--only-remapping] [--debug] [source file] [target file]" + System.lineSeparator()
                + "Available Mappings:");

        for (MappingInterface mapping : Mapping.getMappings()) {
            usage
                    .append(System.lineSeparator())
                    .append("  - ")
                    .append(mapping.getName());
        }

        return usage.toString();
    }

    @Override
    public boolean execute() throws Throwable {
        if (this.getParameters().length < 2) {
            this.getCommandManager().dispatch(CommandManager.HELP_COMMAND, new String[] { this.getName() });
            return true;
        }

        boolean debug = this.isArgument("debug");
        boolean onlyRemapping = this.isArgument("only-remapping");
        Pattern matchAlertPattern = this.isArgument("match-alert")? Pattern.compile(this.getArgument("match-alert")): null;

        String fromMappingName = this.getArgument("from-mapping");
        MappingInterface fromMapping = Mapping.getMapping(fromMappingName);
        if (fromMappingName.length() > 0 && fromMapping == null) {
            System.out.println("Couldn't find mapping '" + fromMappingName + "'");
            return true;
        }

        String toMappingName = this.getArgument("to-mapping");
        MappingInterface toMapping = Mapping.getMapping(toMappingName);
        if (toMappingName.length() > 0 && toMapping == null) {
            System.out.println("Couldn't find mapping '" + toMappingName + "'");
            return true;
        }

        if (fromMapping == null && toMapping == null) {
            System.out.println("No mappings specified.");
            return true;
        }

        Path sourceFilePath = Paths.get(this.getParameters()[0]).toAbsolutePath();
        System.out.println("Loading source file '" + sourceFilePath.getFileName() + "'");
        ArkSavegame savegame = new ArkSavegame(sourceFilePath);
        List<GameObject> objects = savegame.getObjects();

        System.out.println("Mapping classes..");
        StringBuilder strBuilder = new StringBuilder();
        int idWidth = String.valueOf(objects.size()).length() + 2;

        String className;
        String classNameReplacement;
        String classNameCached;
        for (GameObject obj : objects) {
            className = obj.getClassString();
            classNameReplacement = className;

            if (className == null) {
                continue;
            }

            if (fromMapping != null) {
                classNameCached = fromMapping.getOrigin(classNameReplacement);

                if (classNameCached != null || onlyRemapping) {
                    classNameReplacement = classNameCached;
                }
            }

            if (toMapping != null) {
                classNameCached = toMapping.getReplacement(classNameReplacement);

                if (classNameCached != null || onlyRemapping) {
                    classNameReplacement = classNameCached;
                }
            }

            if (classNameReplacement != null && !className.equals(classNameReplacement)) {
                if (debug) {
                    strBuilder.append(System.lineSeparator());
                    strBuilder.append("# ");
                    strBuilder.append(StringC.pad(Pad.RIGHT, String.valueOf(obj.getId()), idWidth));
                    strBuilder.append(StringC.pad(Pad.RIGHT, className, 60));
                    strBuilder.append(" => ");
                    strBuilder.append(classNameReplacement);
                }

                obj.setClassString(classNameReplacement);
            } else {
                if (debug) {
                    strBuilder.append(System.lineSeparator());
                    strBuilder.append("# ");
                    strBuilder.append(StringC.pad(Pad.RIGHT, String.valueOf(obj.getId()), idWidth));
                    strBuilder.append(StringC.pad(Pad.RIGHT, className, 60));
                    strBuilder.append(" => (not replacing) ");
                    strBuilder.append(classNameReplacement == null? "null": classNameReplacement);
                }
                if (classNameReplacement == null) {
                    if (matchAlertPattern != null && matchAlertPattern.matcher(className).matches()) {
                        strBuilder.append(System.lineSeparator());
                        strBuilder.append("# ");
                        strBuilder.append(StringC.pad(Pad.RIGHT, String.valueOf(obj.getId()), idWidth));
                        strBuilder.append("Failed to map class: ");
                        strBuilder.append(className);
                    }
                }
            }
        }
        System.out.println(strBuilder);

        Path targetFilePath = Paths.get(this.getParameters()[1]).toAbsolutePath();
        System.out.println("Saving target file '" + targetFilePath.getFileName() + "'");
        savegame.writeBinary(targetFilePath);

        return true;
    }

}