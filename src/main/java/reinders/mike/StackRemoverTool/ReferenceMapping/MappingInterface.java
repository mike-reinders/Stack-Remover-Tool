package reinders.mike.StackRemoverTool.ReferenceMapping;

import java.util.Map;

public interface MappingInterface {

    String getName();

    Map<String, String> getMapping();

    default String getReplacement(String origin) {
        if (origin != null) {
            for (Map.Entry<String, String> entry : this.getMapping().entrySet()) {
                if (entry.getKey().equals(origin)) {
                    return entry.getValue();
                }
            }
        }

        return null;
    }

    default String getOrigin(String replacement) {
        if (replacement != null) {
            for (Map.Entry<String, String> entry : this.getMapping().entrySet()) {
                if (entry.getValue().equals(replacement)) {
                    return entry.getKey();
                }
            }
        }

        return null;
    }

}