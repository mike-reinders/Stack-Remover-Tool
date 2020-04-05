package reinders.mike.StackRemoverTool.ClassMapping;

import java.util.Map;

public interface MappingInterface {

    public String getName();

    public Map<String, String> getMapping();

    public default String getReplacement(String origin) {
        for (Map.Entry<String, String> entry : this.getMapping().entrySet()) {
            if (entry.getKey().equals(origin)) {
                return entry.getValue();
            }
        }

        return null;
    }

    public default String getOrigin(String replacement) {
        for (Map.Entry<String, String> entry : this.getMapping().entrySet()) {
            if (entry.getValue().equals(replacement)) {
                return entry.getKey();
            }
        }

        return null;
    }

}