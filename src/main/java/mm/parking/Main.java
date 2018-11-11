package mm.parking;

import mm.parking.cli.Argument;
import mm.parking.cli.CommandLine;

import java.io.File;
import java.util.List;

public class Main {
    public static final String ARGUMENT_DATA_TYPE = "datatype";
    public static final String ARGUMENT_FILE_FORMAT = "format";
    public static final String ARGUMENT_DIR_PATH = "dir";
    public static final String ARGUMENT_HELP = "help";

    public static final String HELP_PRINTOUT_FORMAT = "%-12s %-35s %-50s\n";

    public static void main(String[] args) {
        var arguments = createArguments();
        var cli = new CommandLine();

        try {
            cli.parse(args, arguments);

            if (cli.isFlagSet(ARGUMENT_HELP)) {
                printHelp();
                System.exit(0);
            }

            System.out.println("Parsing arguments ...");

            var dataTargets = cli.getArgumentTarget(ARGUMENT_DATA_TYPE);
            if (dataTargets.isEmpty()) {
                System.out.printf("Required argument --%s not set.", ARGUMENT_DATA_TYPE);
                System.out.println();
                System.exit(-1);
            } else {
                // only one target is expected, discard others if specified
                var target = dataTargets.get(0);

            }

            var fileFormats = cli.getArgumentTarget(ARGUMENT_FILE_FORMAT);
            if (fileFormats.isEmpty()) {
                System.out.printf("--%s not set, using default value.", ARGUMENT_FILE_FORMAT);
                System.out.println();
            } else {
                System.out.println("Output file formats: " + fileFormats);
            }

            var directoryTarget = cli.getArgumentTarget(ARGUMENT_DIR_PATH);
            if (directoryTarget.isEmpty()) {
                System.out.printf("--%s not set, using default value.", ARGUMENT_DIR_PATH);
                System.out.println();
            } else {
                // only one target is expected
                var dirPath = directoryTarget.get(0);
                var file = new File(dirPath);
                if (!file.isDirectory()) {
                    System.out.println("Not a directory: " + dirPath);
                    System.exit(-1);
                }

                System.out.println("Output directory: " + dirPath);
            }

            System.out.println("Parsing arguments done.");

        } catch (IllegalArgumentException e) {
            System.out.printf("Failed to parse arguments [%s]\n", e.getMessage());
        }
    }

    private static List<Argument> createArguments() {
        return List.of(
                new Argument(ARGUMENT_DATA_TYPE, false, true),
                new Argument(ARGUMENT_FILE_FORMAT, false, false),
                new Argument(ARGUMENT_DIR_PATH, false, false),
                new Argument(ARGUMENT_HELP, true, false));
    }

    private static void printHelp() {
        System.out.println("ParkingParser help");
        var arg = "Argument";
        var values = "Values";
        var description = "Description";
        System.out.printf(HELP_PRINTOUT_FORMAT, arg, values, description);
        System.out.println();

        arg = ARGUMENT_DATA_TYPE;
        values = "[price] [time] [location] [all]";
        description = "- Download data based on input.";
        System.out.printf(HELP_PRINTOUT_FORMAT, arg, values, description);

        arg = ARGUMENT_FILE_FORMAT;
        values = "[json] [xml] [raw] [all]";
        description = "- Specify file formats in which to store downloaded data. " +
                "If argument is not set then data is stored in all supported formats.";
        System.out.printf(HELP_PRINTOUT_FORMAT, arg, values, description);

        arg = ARGUMENT_DIR_PATH;
        values = "";
        description = "- Path to existing file system directory in which to store downloaded data. " +
                "If argument is not set then the file is stored in calling dir.";
        System.out.printf(HELP_PRINTOUT_FORMAT, arg, values, description);
    }
}
