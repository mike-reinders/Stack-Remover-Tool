package reinders.mike.StackRemoverTool.CommandTool;

import qowyn.ark.ArkArchive;
import reinders.mike.StackRemoverTool.Command.Command;
import reinders.mike.StackRemoverTool.ReferenceMapping.Mapping;
import reinders.mike.StackRemoverTool.ReferenceMapping.MappingInterface;
import reinders.mike.StackRemoverTool.Util.Pad;
import reinders.mike.StackRemoverTool.Util.StringC;

import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class DebugMaxQuantitiesCommand extends Command {

    @Override
    public String getName() {
        return "debug-max-quantities";
    }

    @Override
    public String getDescription() {
        return "Finds all stacking sizes by mod folder and blueprint reference mapping";
    }

    @Override
    public String getUsage() {
        return "[--no-null] [--origin-only] [--produce-origin-config] [mod directory] [mapping]";
    }

    @Override
    public boolean execute() throws Throwable {
        if (this.getParameters().length < 2) {
            this.getCommandManager().dispatch(CommandManager.HELP_COMMAND, new String[] { this.getName() });
            return true;
        }

        boolean noNull = this.isArgument("no-null");
        boolean originOnly = this.isArgument("origin-only");
        boolean produceOriginConfig = this.isArgument("produce-origin-config");
        Path modPath = Paths.get(this.getParameters()[0]);

        String mappingName = this.getParameters()[1];
        MappingInterface mapping = Mapping.getMapping(mappingName);
        if (mapping == null) {
            System.out.println("Couldn't find mapping '" + mappingName + "'");
            return true;
        }

        StringBuilder strBuilder = new StringBuilder();

        ArrayList<QuantityMap> quantityMaps = this.findMaxItemQuantities(modPath, mapping);
        quantityMaps.sort((a, b) -> {
            if (a.maxItemQuantity == null && b.maxItemQuantity == null) {
                return a.origin.compareTo(b.origin);
            }

            if (a.maxItemQuantity == null) {
                return 1;
            } else if (b.maxItemQuantity == null) {
                return -1;
            }

            if (b.maxItemQuantity.equals(a.maxItemQuantity)) {
                return a.origin.compareTo(b.origin);
            }

            return b.maxItemQuantity - a.maxItemQuantity;
        });

        if (produceOriginConfig) {
            int offset;
            for (QuantityMap quantityMap : quantityMaps) {
                if (quantityMap.maxItemQuantity != null) {
                    if ((offset = quantityMap.origin.lastIndexOf(".")) < 0) {
                        offset = 0;
                    } else {
                        offset++;
                    }

                    strBuilder.append(System.lineSeparator());
                    strBuilder.append("ConfigOverrideItemMaxQuantity=(ItemClassString=\"");
                    strBuilder.append(quantityMap.origin.substring(offset));
                    strBuilder.append("\",Quantity=(MaxItemQuantity=");
                    strBuilder.append(quantityMap.maxItemQuantity);
                    strBuilder.append(",bIgnoreMultiplier=true))");
                } else if (!noNull) {
                    strBuilder.append(System.lineSeparator());
                    strBuilder.append("!!! ");
                    strBuilder.append(quantityMap.replacement);
                }
            }
        } else {
            for (QuantityMap quantityMap : quantityMaps) {
                if (quantityMap.maxItemQuantity != null || !noNull) {
                    strBuilder.append(System.lineSeparator());
                    strBuilder.append(System.lineSeparator());
                    if (!originOnly) {
                        strBuilder.append(StringC.pad(Pad.RIGHT, quantityMap.replacement, 170));
                        strBuilder.append(System.lineSeparator());
                    }
                    strBuilder.append("     => ");
                    strBuilder.append(StringC.pad(Pad.RIGHT, quantityMap.origin, 170));
                    strBuilder.append("MaxItemQuantity: ");
                    strBuilder.append(quantityMap.maxItemQuantity);
                }
            }
        }

        System.out.println(strBuilder);

        return true;
    }

    public ArrayList<QuantityMap> findMaxItemQuantities(Path modPath, MappingInterface referenceMapping) {
        ArrayList<QuantityMap> list = new ArrayList<>();

        Integer maxQuantityValue;
        QuantityMap quantityMap;
        for (Map.Entry<String, String> entry : referenceMapping.getMapping().entrySet()) {
            try {
                maxQuantityValue = this.readMaxItemQuantityValue(
                        Paths.get(modPath.toAbsolutePath().toString(), getCleanBlueprintPath(entry.getValue()) + ".uasset")
                );
            } catch (Exception ignored) {
                break;
            }

            quantityMap = new QuantityMap();
            quantityMap.origin = entry.getKey();
            quantityMap.replacement = entry.getValue();
            quantityMap.maxItemQuantity = maxQuantityValue;

            list.add(quantityMap);
        }

        return list;
    }

    public static class QuantityMap {
        public String origin;
        public String replacement;
        public Integer maxItemQuantity;
    }

    public String getCleanBlueprintPath(String path) {
        int offset, end;

        // remove generic mods folder path
        if (path.startsWith("/Game/Mods")) {
            offset = "/Game/Mods".length();
        } else {
            offset = 0;
        }

        // remove mod folder
        if ((offset = path.indexOf("/", offset + 1)) < 0) {
            offset = 0;
        } else {
            offset++;
        }

        // remove class name
        if ((end = path.lastIndexOf(".")) <= offset) {
            end = path.length();
        }

        return path.substring(offset, end);
    }

    public Integer readMaxItemQuantityValue(Path filePath) throws IOException {
        ArkArchive archive;
        try (FileChannel fc = FileChannel.open(filePath, StandardOpenOption.READ)) {
            ByteBuffer buffer = ByteBuffer.allocateDirect((int) fc.size());
            int bytesRead = fc.read(buffer);
            int totalRead = bytesRead;
            while (bytesRead != -1 && totalRead < fc.size()) {
                bytesRead = fc.read(buffer);
                totalRead += bytesRead;
            }
            buffer.clear();

            archive = new ArkArchive(buffer, filePath);
        }

        assertV(archive.getLong() == -10231315519L); // file type?
        assertV(archive.getLong() == 1735166788448L); // file type?
        assertV(archive.getInt() == 10); // file format version?

        // skip unknown 1 count & offset
        int unknown1count = archive.getInt();
        archive.skipBytes(4);
        assertV(unknown1count == 0);

        archive.skipString();

        assertV(archive.getInt() == Integer.MIN_VALUE); // unknown

        int namesCount = archive.getInt();
        int namesOffset = archive.getInt();

        // skip properties count & offset
        archive.skipBytes(8);

        // skip hibernation count
        archive.skipBytes(4);
        int hibernationOffset = archive.getInt();

        // skip unknown offset 4
        archive.skipBytes(4);
        assertV(archive.getInt() == 0);

        // skip unknown offset 5
        archive.skipBytes(4);
        assertV(archive.getInt() == 0);

        // skip UUID
        archive.skipBytes(16);

        // skip unknown offset 6, 7 and second count
        archive.skipBytes(12);

        for (byte bt : archive.getBytes(22)) {
            assertV(bt == 0);
        }

        // unknown offset 8
        archive.skipBytes(4);

        for (byte bt : archive.getBytes(16)) {
            assertV(bt == 0);
        }

        // unknown offset 8 & 9
        archive.skipBytes(8);

        // Zero
        assertV(archive.getInt() == 0);
        assertV(archive.getInt() == 0);
        assertV(archive.getInt() == 0);

        archive.position(namesOffset);
        List<String> names = new ArrayList<>();

        for (int i = 0; i < namesCount; i++) {
            names.add(archive.getString());
        }

        //********************** finally find max item quantity value **********************/

        int maxItemQuantityIndex = names.indexOf("MaxItemQuantity");
        int intPropertyIndex = names.indexOf("IntProperty");

        if (maxItemQuantityIndex > 0 && intPropertyIndex > 0) {
            try {
                Integer maxItemQuantityValue = null;
                for (int i = hibernationOffset; i < (archive.limit() - 4); i++) {
                    archive.position(i);

                    if (archive.getInt() == maxItemQuantityIndex
                        && archive.getInt() == 0
                        && archive.getInt() == intPropertyIndex
                        && archive.getInt() == 0
                        && archive.getInt() == 4
                        && archive.getInt() == 0
                    ) {
                        if ((maxItemQuantityValue = archive.getInt()) >= 0) {
                            return maxItemQuantityValue;
                        }
                    }
                }
            } catch (BufferUnderflowException ignored) {}
        }

        return null;
    }

    protected static void assertV(boolean value) {
        if (!value) {
            throw new AssertionError("UAsset assertion failed");
        }
    }

}