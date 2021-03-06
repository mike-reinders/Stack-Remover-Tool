package reinders.mike.StackRemoverTool.Command;

import reinders.mike.StackRemoverTool.Exception.MissingArgumentException;

import java.util.*;

public abstract class Command {

    private CommandManager commandManager;

    private String commandName;
    private HashMap<String, String[]> arguments;
    private String[] parameters;

    public abstract String getName();

    public String[] getAlias() {
        return null;
    }

    public String getDescription() {
        return null;
    }

    public final String getDescriptionString() {
        String description =  this.getDescription();

        return description == null? "": description;
    }

    public String getUsage() {
        return null;
    }

    public final String getUsageString() {
        String usage =  this.getUsage();

        return usage == null? "": usage;
    }

    public abstract boolean execute() throws Throwable;

    final void setCommandManager(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    public final CommandManager getCommandManager() {
        return this.commandManager;
    }

    final void setCommandName(String name) {
        this.commandName = name;
    }

    public final String getCommandName() {
        return this.commandName;
    }

    final void setArguments(HashMap<String, String[]> arguments) {
        this.arguments = arguments;
    }

    public final Set<Map.Entry<String, String[]>> getArguments() {
        return this.arguments.entrySet();
    }

    final void setParameters(String[] parameters) {
        this.parameters = parameters;
    }

    public final String[] getParameters() {
        return this.parameters;
    }

    public final Integer getInteger(String ...names) throws MissingArgumentException {
        try {
            return Integer.parseInt(this.requireArgument(names));
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(String.join(" ", names));
        }
    }

    public final Float getFloat(String ...names) throws MissingArgumentException {
        try {
            return Float.parseFloat(this.requireArgument(names));
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(String.join(" ", names));
        }
    }

    public final Boolean getBoolean(String ...names) throws MissingArgumentException {
        String argument = this.requireArgument(names);

        if (argument.equalsIgnoreCase("true")) {
            return true;
        } else if (argument.equalsIgnoreCase("false")) {
            return false;
        } else {
            throw new IllegalArgumentException(String.join(" ", names));
        }
    }

    public final String requireArgument(String ...names) throws MissingArgumentException {
        String argument = this.getArgumentDefault(null, names);

        if (argument == null) {
            throw new MissingArgumentException("Missing argument '" + String.join(" ", names) + "'");
        }

        return argument;
    }

    public final String getArgument(String ...names) {
        return this.getArgumentDefault("", names);
    }

    public final String getArgumentDefault(String defaultValue, String ...names) {
        String[] arguments;

        if (names.length == 1) {
            arguments = this.getArguments(names[0]);
        } else {
            arguments = this.getArguments(names);
        }

        if (arguments.length > 0) {
            return arguments[arguments.length - 1];
        }

        return defaultValue;
    }

    public final String[] getArguments(String ...names) {
        List<String[]> foundItems = new ArrayList<>();

        if (names.length > 0) {
            for (Map.Entry<String, String[]> entry : this.getArguments()) {
                for (String name : names) {
                    if (entry.getKey().equals(name)) {
                        foundItems.add(entry.getValue());
                    }
                }
            }
        }

        if (foundItems.size() == 0) {
            return new String[0];
        } else if (foundItems.size() == 1) {
            return foundItems.get(0);
        } else {
            int totalLength = 0;
            for (String[] items : foundItems) {
                totalLength += items.length;
            }

            String[] finalItems = new String[totalLength];
            int index = 0;
            for (String[] items : foundItems) {
                System.arraycopy(items, 0, finalItems, index, items.length);
                index += items.length;
            }

            return finalItems;
        }
    }

    public final String[] requireArguments(String name) throws MissingArgumentException {
        String[] value = this.getArguments(name);

        if (value == null) {
            throw new MissingArgumentException("Missing argument '" + name + "'");
        }

        return value;
    }

    public final boolean isArgument(String name) {
        for (Map.Entry<String, String[]> entry : this.getArguments()) {
            if (entry.getKey().equals(name)) {
                return true;
            }
        }

        return false;
    }

    public final int getOption(String ...names) {
        Boolean[] options = new Boolean[names.length];

        for (int i = 1; i < names.length; i++) {
            if (this.isArgument(names[i])) {
                return i;
            }
        }

        return 0;
    }

    public final int getOptionDefault(String ...names) {
        return this.getOptionDefault(1, names);
    }

    public final int getOptionDefault(int defaultOption, String ...names) {
        int option = this.getOption(names);

        if (option > 0) {
            return option;
        }

        return defaultOption;
    }



}