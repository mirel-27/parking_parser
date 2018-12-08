package mm.parking.cli;

import java.util.*;

public class CommandLine {
    private String[] args;
    private Map<String, Integer> parsedArguments = new HashMap<>();

    public void parse(String[] args, List<Argument> arguments) {
        parsedArguments.clear();
        this.args = args;

        for (int i = 0; i < args.length; i++) {
            if (args[i].startsWith("--")) {
                String argString = args[i].substring(2);
                Argument argument = findArgument(argString, arguments);
                if (argument == null) {
                    throw new IllegalArgumentException("Unknown arg = " + argString);
                }

                parsedArguments.put(argument.getName(), i);
            }
        }
    }

    public List<String> getArgumentTarget(String arg) {
        Integer index = parsedArguments.get(arg);
        if (index == null) {
            return Collections.emptyList();
        }

        List<String> targets = new ArrayList<>();

        do {
            index++;
            targets.add(args[index]);
        } while ((index < args.length - 1) && !(args[index + 1].startsWith("--")));

        return targets;
    }

    public boolean isFlagSet(String arg) {
        return parsedArguments.containsKey(arg);
    }

    private Argument findArgument(String arg, List<Argument> arguments) {
        for (Argument o : arguments) {
            if (o.getName().equals(arg)) {
                return o;
            }
        }

        return null;
    }
}
