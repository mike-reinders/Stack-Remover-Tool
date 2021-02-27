package reinders.mike.StackRemoverTool.CommandTool;

import qowyn.ark.ArkArchive;
import reinders.mike.StackRemoverTool.Command.Command;
import reinders.mike.StackRemoverTool.Util.Pad;
import reinders.mike.StackRemoverTool.Util.StringC;
import reinders.mike.StackRemoverTool.Util.ThrowableC;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DumpModCommand extends Command {

    @Override
    public String getName() {
        return "dump-mod";
    }

    @Override
    public String getDescription() {
        return "Dumps all references and classes by a given path";
    }

    @Override
    public String getUsage() {
        return "[mod directory]";
    }

    @Override
    public boolean execute() throws Throwable {
        if (this.getParameters().length < 1) {
            this.getCommandManager().dispatch(CommandManager.HELP_COMMAND, new String[] { this.getName() });
            return true;
        }

        String pattern;
        if (this.getParameters().length >= 2) {
            pattern = this.getParameters()[1];
        } else {
            pattern = "$1 => $2";
        }

        /* debugFile(Paths.get("D:\\Steam\\steamapps\\common\\ARK\\ShooterGame\\Content\\Mods\\761535755\\Weapons\\PrimalItem_PlantSpeciesZ_Grenade_US.uasset")); */
        /* debugFile(Paths.get("D:\\Steam\\steamapps\\common\\ARK\\ShooterGame\\Content\\Mods\\761535755\\Seeds\\PrimalItemConsumable_Seed_Savoroot_US.uasset")); */
        /* debugFile(Paths.get("D:\\Steam\\steamapps\\common\\ARK\\ShooterGame\\Content\\Mods\\842913750\\Consumables\\PrimalItemConsumable_Berry_Narcoberry_Child.uasset")); */

        debugFile(Paths.get("D:\\Steam\\steamapps\\common\\ARK\\ShooterGame\\Content\\Mods\\2047318996\\Ammo\\PrimalItemAmmo_RefinedTranqDart_Child.uasset"));

        Path modPath = Paths.get(this.getParameters()[0]);

        System.out.println("Using pattern: " + pattern);

        Files.walk(modPath)
        .filter(Files::isRegularFile)
        .filter(path -> path.relativize(modPath).toString().contains("\\"))
        .filter(path -> path.getFileName().toString().endsWith(".uasset"));
        /*.forEach(path -> {
            /// System.out.println();
            /// System.out.println(modPath.relativize(path));

            try {
                UAssetMappingContent uAssetMappingContent = UAssetMappingContent.fromFile(path, false);

                if (!uAssetMappingContent.getClassOrigin().startsWith("EngramEntry_")
                    && !uAssetMappingContent.getClassReplacement().startsWith("EngramEntry_")
                ) {
                    if (this.isArgument("blueprints")) {
                        System.out.println(
                                pattern
                                .replace("$1", uAssetMappingContent.getBlueprintOrigin())
                                .replace("$2", uAssetMappingContent.getBlueprintReplacement())
                        );
                    } else if (this.isArgument("classes")) {
                        System.out.println(
                                pattern
                                .replace("$1", uAssetMappingContent.getClassOrigin())
                                .replace("$2", uAssetMappingContent.getClassReplacement())
                        );
                    }
                }
            } catch (Throwable throwable) {
                //System.out.println(ThrowableC.toString(throwable));
                System.out.println("Error");
            }
        });*/

        return true;
    }

    protected void debugFile(Path path) {
        System.out.println();
        System.out.println();
        System.out.println(path);

        try {
            UAssetMappingContent uAssetMappingContent = UAssetMappingContent.fromFile(path, true);

            System.out.print(uAssetMappingContent.getBlueprintOrigin());
            System.out.print(" => ");
            System.out.println(uAssetMappingContent.getBlueprintReplacement());

            System.out.print(uAssetMappingContent.getClassOrigin());
            System.out.print(" => ");
            System.out.println(uAssetMappingContent.getClassReplacement());
        } catch (Throwable throwable) {
            System.out.println(ThrowableC.toString(throwable));
        }
    }

    protected static class UAssetMappingContent {

        private String blueprintOrigin;
        private String blueprintReplacement;

        private String classOrigin;
        private String classReplacement;

        private UAssetMappingContent() {
            // Private
        }

        public static UAssetMappingContent fromFile(Path filePath) throws IOException {
            return UAssetMappingContent.fromFile(filePath, false);
        }

        public static UAssetMappingContent fromFile(Path filePath, boolean debug) throws IOException {
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

            int leftColumnWidth = 30;

            assertV(archive.getLong() == -10231315519L); // file type?
            assertV(archive.getLong() == 1735166788448L); // file type?
            assertV(archive.getInt() == 10); // file format version?

            int unknown1count = archive.getInt(); // count?
            int unknown1offset = archive.getInt(); // offset?
            assertV(unknown1count == 0);

            if (debug) {
                System.out.println(StringC.pad(Pad.LEFT, "unknown 1 count:  ", leftColumnWidth) + unknown1count);
                System.out.println(StringC.pad(Pad.LEFT, "unknown 1 offset:  ", leftColumnWidth) + unknown1offset);
                System.out.println(archive.getString());
            } else {
                archive.skipString();
            }

            assertV(archive.getInt() == Integer.MIN_VALUE); // unknown

            int namesCount = archive.getInt();
            int namesOffset = archive.getInt();

            int propertiesCount = archive.getInt();
            int propertiesOffset = archive.getInt();

            int hibernationCount = archive.getInt();
            int hibernationOffset = archive.getInt();

            if (debug) {
                System.out.println(StringC.pad(Pad.LEFT, "properties count:  ", leftColumnWidth) + propertiesCount);
                System.out.println(StringC.pad(Pad.LEFT, "properties offset:  ", leftColumnWidth) + propertiesOffset);

                System.out.println(StringC.pad(Pad.LEFT, "hibernation count:  ", leftColumnWidth) + hibernationCount);
                System.out.println(StringC.pad(Pad.LEFT, "hibernation offset:  ", leftColumnWidth) + hibernationOffset);

                System.out.println(StringC.pad(Pad.LEFT, "names count:  ", leftColumnWidth) + namesCount);
                System.out.println(StringC.pad(Pad.LEFT, "names offset:  ", leftColumnWidth) + namesOffset);
            }

            // Offsets
            int unknown4offset = archive.getInt();
            assertV(archive.getInt() == 0);

            int unknown5offset = archive.getInt();
            assertV(archive.getInt() == 0);

            if (debug) {
                System.out.println(StringC.pad(Pad.LEFT, "unknown 4 offset:  ", leftColumnWidth) + unknown4offset);

                System.out.println(StringC.pad(Pad.LEFT, "unknown 5 offset:  ", leftColumnWidth) + unknown5offset);
            }

            // UUID?
            long highOfHigh = archive.getInt();
            long lowOfHigh = archive.getInt();
            long high = (highOfHigh << 32) + lowOfHigh;

            long highOfLow = archive.getInt();
            long lowOfLow = archive.getInt();
            long low = (highOfLow << 32) + lowOfLow;

            UUID uuid = new UUID(high, low);

            if (debug) {
                System.out.println(StringC.pad(Pad.LEFT, "object uuid 1:  ", leftColumnWidth) + uuid.toString());
            }

            int unknown6 = archive.getInt();
            int unknown7 = archive.getInt();

            int secondCount = archive.getInt();

            if (debug) {
                System.out.println(StringC.pad(Pad.LEFT, "unknown 6:  ", leftColumnWidth) + unknown6);
                System.out.println(StringC.pad(Pad.LEFT, "unknown 7:  ", leftColumnWidth) + unknown7);

                System.out.println(StringC.pad(Pad.LEFT, "second count:  ", leftColumnWidth) + secondCount);
                System.out.println(StringC.pad(Pad.LEFT, "@", leftColumnWidth) + archive.position());
            }

            for (byte bt : archive.getBytes(22)) {
                assertV(bt == 0);
            }

            if (debug) {
                System.out.println(StringC.pad(Pad.LEFT, "zeros", leftColumnWidth) + " 22x");
            }

            int unknown8 = archive.getInt();

            if (debug) {
                System.out.println(StringC.pad(Pad.LEFT, "unknown 8:  ", leftColumnWidth) + unknown8);
            }

            for (byte bt : archive.getBytes(16)) {
                assertV(bt == 0);
            }

            if (debug) {
                System.out.println(StringC.pad(Pad.LEFT, "zeros", leftColumnWidth) + " 16x");
            }

            // Offsets
            int unknown8offset = archive.getInt();
            // points to last possible integer offset
            int unknown9offset = archive.getInt();

            if (debug) {
                System.out.println(StringC.pad(Pad.LEFT, "unknown 8 offset:  ", leftColumnWidth) + unknown8offset);
                System.out.println(StringC.pad(Pad.LEFT, "unknown 9 offset:  ", leftColumnWidth) + unknown9offset);
            }

            // Zero
            assertV(archive.getInt() == 0);
            assertV(archive.getInt() == 0);
            assertV(archive.getInt() == 0);

            if (debug) {
                System.out.println(StringC.pad(Pad.LEFT, "@", leftColumnWidth) + archive.position());
            }

            archive.position(namesOffset);
            List<String> names = new ArrayList<>();

            for (int i = 0; i < namesCount; i++) {
                names.add(archive.getString());
                System.out.println(names.get(i));
            }

            if (debug) {
                System.out.println("hibernation size: " + (archive.limit() - hibernationOffset));
            }

            //********************** dirty code **********************/

            // Raw Filename
            String rawFilename = filePath.getFileName().toString();
            rawFilename = rawFilename.substring(0, rawFilename.length() - 7);

            // MappingContent
            UAssetMappingContent uAssetMappingContent = new UAssetMappingContent();

            uAssetMappingContent.setClassReplacement(
                    rawFilename + "_C"
            );

            if (debug) {
                System.out.println("Class Replacement: " + uAssetMappingContent.getClassReplacement());
            }

            boolean foundReplacementClass = false;
            for (String name : names) {
                if (name.equals(uAssetMappingContent.getClassReplacement())) {
                    foundReplacementClass = true;
                    break;
                }
            }

            if (!foundReplacementClass) {
                throw new RuntimeException("Unexpectedly missing replacement class");
            }

            uAssetMappingContent.setClassOrigin(
                    uAssetMappingContent.getClassReplacement()
                    .substring(0, (uAssetMappingContent.getClassReplacement().length() - 8))
                    + "_C"
            );

            if (debug) {
                System.out.println("Class Origin: " + uAssetMappingContent.getClassOrigin());
            }

            boolean foundOriginClass = false;
            for (String name : names) {
                if (name.equals(uAssetMappingContent.getClassOrigin())) {
                    foundOriginClass = true;
                    break;
                }
            }

            if (!foundOriginClass) {
                throw new RuntimeException("Unexpectedly missing origin class");
            }

            for (String name : names) {
                if (name.startsWith("/Game/Mods/Stack50/") && name.endsWith(rawFilename)) {
                    uAssetMappingContent.setBlueprintReplacement(name);
                    break;
                }
            }

            if (debug) {
                System.out.println("Blueprint Origin: " + uAssetMappingContent.getBlueprintOrigin());
            }

            if (uAssetMappingContent.getBlueprintReplacement() == null) {
                throw new RuntimeException("Unexpectedly missing replacement blueprint");
            }

            String classOriginPathname = uAssetMappingContent.getClassOrigin().substring(
                    0,
                    uAssetMappingContent.getClassOrigin().length() - 2
            );
            for (String name : names) {
                if (name.startsWith("/Game/") && !name.startsWith("/Game/Mods/Stack500_50/") && name.endsWith(classOriginPathname)) {
                    uAssetMappingContent.setBlueprintOrigin(name);
                    break;
                }
            }

            if (debug) {
                System.out.println("Blueprint Replacement: " + uAssetMappingContent.getBlueprintOrigin());
            }

            if (uAssetMappingContent.getBlueprintOrigin() == null) {
                throw new RuntimeException("Unexpectedly missing origin blueprint");
            }

            uAssetMappingContent.setBlueprintOrigin(
                    uAssetMappingContent.getBlueprintOrigin()
                            + "."
                            + uAssetMappingContent.getClassOrigin()
            );

            uAssetMappingContent.setBlueprintReplacement(
                    uAssetMappingContent.getBlueprintReplacement()
                            + "."
                            + uAssetMappingContent.getClassReplacement()
            );

            // properties offset + 1?
            //System.out.println("Count: " + ((archive.limit() - propertiesOffset) / 4));

            /* read actual properties */
            /*for (int i = 2312, val; i < (2596 - 4); i += 4) {
                archive.position(i);
                val = archive.getInt();

                System.out.println("at " + i + " (4 bytes) = " + val + (val > 0 && val < names.size()? (" => " + names.get(val)): ""));
            }**/

            //System.out.println("Leftover data:");

            /* read leftover data from properties container */
            /*for (int i = propertiesOffset, val; i < (archive.limit() - 4); i += 4) {
                if (i < 2312 || i >= 2596) {
                    archive.position(i);
                    val = archive.getInt();

                    System.out.println("at " + i + " (4 bytes) = " + val + (val > 0 && val < names.size()? (" => " + names.get(val)): ""));
                }
            }**/

            return uAssetMappingContent;
        }

        protected static void assertV(boolean value) {
            if (!value) {
                throw new AssertionError("UAsset assertion failed");
            }
        }

        public String getBlueprintOrigin() {
            return this.blueprintOrigin;
        }

        protected void setBlueprintOrigin(String blueprintOrigin) {
            this.blueprintOrigin = blueprintOrigin;
        }

        public String getBlueprintReplacement() {
            return this.blueprintReplacement;
        }

        protected void setBlueprintReplacement(String blueprintReplacement) {
            this.blueprintReplacement = blueprintReplacement;
        }

        public String getClassOrigin() {
            return this.classOrigin;
        }

        protected void setClassOrigin(String classOrigin) {
            this.classOrigin = classOrigin;
        }

        public String getClassReplacement() {
            return this.classReplacement;
        }

        protected void setClassReplacement(String classReplacement) {
            this.classReplacement = classReplacement;
        }

    }

}