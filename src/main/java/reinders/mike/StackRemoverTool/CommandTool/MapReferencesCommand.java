package reinders.mike.StackRemoverTool.CommandTool;

import qowyn.ark.ArkSavegame;
import qowyn.ark.GameObject;
import qowyn.ark.properties.Property;
import qowyn.ark.properties.PropertyObject;
import qowyn.ark.types.ArkName;
import qowyn.ark.types.ObjectReference;
import reinders.mike.StackRemoverTool.ReferenceMapping.Mapping;
import reinders.mike.StackRemoverTool.ReferenceMapping.MappingInterface;
import reinders.mike.StackRemoverTool.Command.Command;
import reinders.mike.StackRemoverTool.Util.Pad;
import reinders.mike.StackRemoverTool.Util.StringC;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Pattern;

public class MapReferencesCommand extends Command {

    @Override
    public String getName() {
        return "map-references";
    }

    @Override
    public String getDescription() {
        return "Replaces all references by given mapping";
    }

    public String getUsage() {
        StringBuilder usage = new StringBuilder("[--match-alert=<regex>] [--from-mapping=<name>] [--to-mapping=<name>] [--only-remapping] [--debug] [source file] [target file]" + System.lineSeparator()
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

        System.out.println("Mapping References..");
        StringBuilder strBuilder = new StringBuilder();
        int idWidth = String.valueOf(objects.size()).length() + 2;

        ObjectReference objectRef;
        String refBlueprintPath;
        String refBlueprintPathReplacement;
        String refBlueprintPathCached;
        for (GameObject obj : objects) {
            for (Property<?> prop : obj.getProperties()) {
                if (prop.getType() == PropertyObject.TYPE) {
                    objectRef = ((PropertyObject)prop).getValue();

                    if (objectRef.getObjectString() == null) {
                        continue;
                    }

                    if (!objectRef.getObjectString().toString().startsWith("BlueprintGeneratedClass ")) {
                        if (debug) {
                            strBuilder.append(System.lineSeparator());
                            strBuilder.append("Not a BlueprintGeneratedClass:");
                            strBuilder.append(System.lineSeparator());
                            strBuilder.append(objectRef.getObjectString().toString());
                        }
                        continue;
                    }

                    refBlueprintPath = objectRef.getObjectString().toString().substring("BlueprintGeneratedClass ".length());
                    refBlueprintPathReplacement = refBlueprintPath;

                    if (fromMapping != null) {
                        refBlueprintPathCached = fromMapping.getOrigin(refBlueprintPathReplacement);

                        if (refBlueprintPathCached != null || !onlyRemapping) {
                            refBlueprintPathReplacement = refBlueprintPathCached;
                        }
                    }

                    if (toMapping != null) {
                        refBlueprintPathCached = toMapping.getReplacement(refBlueprintPathReplacement);

                        if (refBlueprintPathCached != null || !onlyRemapping) {
                            refBlueprintPathReplacement = refBlueprintPathCached;
                        }
                    }

                    if (refBlueprintPathReplacement != null && !refBlueprintPath.equals(refBlueprintPathReplacement)) {
                        if (debug) {
                            strBuilder.append(System.lineSeparator());
                            strBuilder.append("# ");
                            strBuilder.append(StringC.pad(Pad.RIGHT, String.valueOf(obj.getId()), idWidth));
                            strBuilder.append(StringC.pad(Pad.RIGHT, refBlueprintPath, 60));
                            strBuilder.append(" => ");
                            strBuilder.append(refBlueprintPathReplacement);
                        }

                        objectRef.setObjectString(ArkName.from("BlueprintGeneratedClass " + refBlueprintPathReplacement));
                    } else {
                        if (debug) {
                            strBuilder.append(System.lineSeparator());
                            strBuilder.append("# ");
                            strBuilder.append(StringC.pad(Pad.RIGHT, String.valueOf(obj.getId()), idWidth));
                            strBuilder.append(StringC.pad(Pad.RIGHT, refBlueprintPath, 60));
                            strBuilder.append(" => (not replacing) ");
                            strBuilder.append(refBlueprintPathReplacement == null? "null": refBlueprintPathReplacement);
                        }
                        if (refBlueprintPathReplacement == null) {
                            if (matchAlertPattern != null && matchAlertPattern.matcher(refBlueprintPath).matches()) {
                                strBuilder.append(System.lineSeparator());
                                strBuilder.append("# ");
                                strBuilder.append(StringC.pad(Pad.RIGHT, String.valueOf(obj.getId()), idWidth));
                                strBuilder.append("Failed to map reference: ");
                                strBuilder.append(refBlueprintPath);
                            }
                        }
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