package reinders.mike.StackRemoverTool.CommandTool;

import qowyn.ark.ArkSavegame;
import qowyn.ark.GameObject;
import qowyn.ark.properties.Property;
import qowyn.ark.properties.PropertyInt;
import qowyn.ark.properties.PropertyObject;
import reinders.mike.StackRemoverTool.Command.Command;
import reinders.mike.StackRemoverTool.Util.Pad;
import reinders.mike.StackRemoverTool.Util.StringC;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.List;
import java.util.regex.Pattern;

public class FindStacksCommand extends Command {

    @Override
    public String getName() {
        return "find-stacks";
    }

    @Override
    public String getDescription() {
        return "Finds all stack sizes by regex";
    }

    public String getUsage() {
        return "[--coord-factor=<double>] [source file] [class regex]";
    }

    @Override
    public boolean execute() throws Throwable {
        if (this.getParameters().length < 2) {
            this.getCommandManager().dispatch(CommandManager.HELP_COMMAND, new String[] { this.getName() });
            return true;
        }

        double coordFactor = Double.parseDouble(this.getArgumentDefault(String.valueOf(8100), "coord-factor"));

        Path sourceFilePath = Paths.get(this.getParameters()[0]).toAbsolutePath();
        System.out.println("Loading file '" + sourceFilePath.getFileName() + "'");
        ArkSavegame savegame = new ArkSavegame(sourceFilePath);
        List<GameObject> objects = savegame.getObjects();

        System.out.println("Looking for stacks..");
        StringBuilder strBuilder = new StringBuilder();
        int idWidth = String.valueOf(objects.size()).length() + 2;

        Pattern filterPattern = Pattern.compile(this.getParameters()[1]);
        DecimalFormat df = new DecimalFormat("#.00");
        String className;
        GameObject ownerInventory;
        GameObject inventoryStructure;
        Integer stackSize;
        for (GameObject obj : objects) {
            className = obj.getClassString();

            if (className == null) {
                continue;
            }

            ownerInventory = null;
            stackSize = null;
            inventoryStructure = null;

            if (filterPattern.matcher(className).matches()) {
                for (Property<?> prop : obj.getProperties()) {
                    if (prop.getType() == PropertyInt.TYPE && prop.getNameString().equals("ItemQuantity")) {
                        stackSize = ((PropertyInt)prop).getValue();
                        break;
                    }
                }

                for (Property<?> prop : obj.getProperties()) {
                    if (prop.getType() == PropertyObject.TYPE && prop.getNameString().equals("OwnerInventory")) {
                        for (GameObject obj2 : objects) {
                            if (obj2.getId() == ((PropertyObject)prop).getValue().getObjectId()) {
                                ownerInventory = obj2;
                                break;
                            }
                        }

                        if (ownerInventory != null) {
                            break;
                        }
                    }
                }

                /* search for structure object with given inventory component **/
                if (ownerInventory != null) {
                    for (GameObject obj2 : objects) {
                        for (Property<?> prop : obj2.getProperties()) {
                            if (prop.getType() == PropertyObject.TYPE && prop.getNameString().equals("MyInventoryComponent")) {
                                if (ownerInventory.getId() == ((PropertyObject)prop).getValue().getObjectId()) {
                                    inventoryStructure = obj2;
                                    break;
                                }
                            }
                        }

                        if (inventoryStructure != null) {
                            break;
                        }
                    }
                }

                if (stackSize == null || ownerInventory == null || inventoryStructure == null) {
                    continue;
                }

                strBuilder.append(System.lineSeparator());
                strBuilder.append("# ");
                strBuilder.append(StringC.pad(Pad.RIGHT, String.valueOf(obj.getId()), idWidth));
                strBuilder.append(StringC.pad(Pad.RIGHT, className, 30));
                strBuilder.append(" | ");
                strBuilder.append("Stack Size: ");
                strBuilder.append(StringC.pad(Pad.RIGHT, String.valueOf(stackSize), 6));
                strBuilder.append(" | ");
                strBuilder.append("Inv. Id: ");
                strBuilder.append(StringC.pad(Pad.RIGHT, String.valueOf(ownerInventory.getId()), 6));
                strBuilder.append(" | ");
                strBuilder.append("Inv. Struc. Id: ");
                strBuilder.append(StringC.pad(Pad.RIGHT, String.valueOf(inventoryStructure.getId()), 6));
                strBuilder.append(" | ");
                strBuilder.append("Inv. Struc. Class: ");
                strBuilder.append(StringC.pad(Pad.RIGHT, String.valueOf(inventoryStructure.getClassString()), 30));
                strBuilder.append(" | ");
                strBuilder.append("Inv. Struc. Loc.: ");
                strBuilder.append(StringC.pad(Pad.RIGHT, df.format(inventoryStructure.getLocation().getY() / coordFactor + 50) + " " + df.format(inventoryStructure.getLocation().getX() / coordFactor + 50), 10));
            }
        }
        System.out.println(strBuilder);

        return true;
    }

}