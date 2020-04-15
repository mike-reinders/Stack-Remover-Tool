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

        System.out.println("Mapping References..");

        boolean debug = this.isArgument("debug");
        StringBuilder strBuilder = new StringBuilder();

        int idWidth = String.valueOf(objects.size()).length() + 2;

        String replacement;
        String origin;
        ObjectReference ref;
        int bpOffset;
        for (GameObject obj : objects) {
            for (Property<?> prop : obj.getProperties()) {
                if (prop.getType() == PropertyObject.TYPE) {
                    ref = ((PropertyObject)prop).getValue();

                    if (ref.getObjectString() != null) {
                        bpOffset = ref.getObjectString().toString().indexOf("/Game/Mods/UltraStacks/");

                        if (bpOffset >= 0) {
                            replacement = ref.getObjectString().toString().substring(bpOffset);
                            origin = mapping.getOrigin(replacement);

                            if (origin == null) {
                                if (debug) {
                                    strBuilder.append(System.lineSeparator());
                                    strBuilder.append("# ");
                                    strBuilder.append(StringC.pad(Pad.RIGHT, String.valueOf(obj.getId()), idWidth));
                                    strBuilder.append("Failed to map replacement reference: ");
                                    strBuilder.append(replacement);
                                }
                            } else {
                                ref.setObjectString(ArkName.from(ref.getObjectString().toString().substring(0, bpOffset) + origin));

                                if (debug) {
                                    strBuilder.append(System.lineSeparator());
                                    strBuilder.append("# ");
                                    strBuilder.append(StringC.pad(Pad.RIGHT, String.valueOf(obj.getId()), idWidth));
                                    strBuilder.append(StringC.pad(Pad.RIGHT, replacement, 60));
                                    strBuilder.append(" => ");
                                    strBuilder.append(origin);
                                }
                            }
                        }
                    }
                }
            }
        }

        System.out.println(strBuilder);

        System.out.println("Saving target file '" + targetFilePath.getFileName() + "'");
        savegame.writeBinary(targetFilePath);

        return true;
    }

}