package reinders.mike.StackRemoverTool.CommandTool;

import qowyn.ark.ArkSavegame;
import qowyn.ark.GameObject;
import qowyn.ark.properties.Property;
import qowyn.ark.properties.PropertyObject;
import qowyn.ark.types.ArkName;
import qowyn.ark.types.ObjectReference;
import reinders.mike.StackRemoverTool.Command.Command;
import reinders.mike.StackRemoverTool.Util.Pad;
import reinders.mike.StackRemoverTool.Util.StringC;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class FindReferencesCommand extends Command {

    @Override
    public String getName() {
        return "find-references";
    }

    @Override
    public String getDescription() {
        return "Finds all object references";
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

        Map<String, List<String>> mapping = new HashMap<>();
        List<GameObject> objects = savegame.getObjects();

        System.out.println("Looking for object references..");

        ArkName typeObjectReference = PropertyObject.TYPE;
        List<String> otherObjRefs;
        List<String> objRefs = new ArrayList<>();
        for (GameObject obj : objects) {
            if (objRefs.size() > 0) {
                objRefs = new ArrayList<>();
            }

            for (Property<?> property : obj.getProperties()) {
                if (property.getType() == typeObjectReference) {
                    if (((PropertyObject)property).getValue().getObjectString() != null) {
                        objRefs.add(property.getNameString() + " => " + ((PropertyObject)property).getValue().getObjectString().toString());
                    }
                }
            }

            if (objRefs.size() > 0) {
                if ((otherObjRefs = mapping.get(obj.getClassString())) == null) {
                    mapping.put(obj.getClassString(), objRefs);
                } else {
                    for (String objRef : objRefs) {
                        if (!otherObjRefs.contains(objRef)) {
                            otherObjRefs.add(objRef);
                        }
                    }
                }
            }
        }

        StringBuilder strBuilder = new StringBuilder();

        if (this.isArgument("filter")) {
            System.out.println("Using filter: " + this.getArgument("filter"));
            System.out.println("Filtering object references..");

            Pattern filterPattern = Pattern.compile(this.getArgument("filter"));
            for (Map.Entry<String, List<String>> entry : mapping.entrySet()) {
                for (String objRef : entry.getValue()) {
                    if (filterPattern.matcher(objRef).matches()) {
                        strBuilder.append(System.lineSeparator());
                        strBuilder.append(entry.getKey());
                        strBuilder.append("#");
                        strBuilder.append(objRef);
                    }
                }
            }
        } else {
            System.out.println("Preparing object references..");

            for (Map.Entry<String, List<String>> entry : mapping.entrySet()) {
                for (String objRef : entry.getValue()) {
                    strBuilder.append(System.lineSeparator());
                    strBuilder.append(entry.getKey());
                    strBuilder.append("#");
                    strBuilder.append(objRef);
                }
            }
        }

        System.out.println("Here are all object references:");
        System.out.println(strBuilder);

        return true;
    }

}