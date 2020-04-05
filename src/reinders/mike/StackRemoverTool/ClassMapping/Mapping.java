package reinders.mike.StackRemoverTool.ClassMapping;

import java.util.ArrayList;
import java.util.List;

public final class Mapping {

    private static List<MappingInterface> mappingList = new ArrayList<>();

    static {
        Mapping.mappingList.add(new UltraStacks());
    }

    private Mapping() {
        // Private
    }

    public static MappingInterface[] getMappings() {
        return Mapping.mappingList.toArray(new MappingInterface[0]);
    }

    public static MappingInterface getMapping(String name) {
        for (MappingInterface mapping : Mapping.mappingList) {
            if (mapping.getName().equalsIgnoreCase(name)) {
                return mapping;
            }
        }

        return null;
    }

}