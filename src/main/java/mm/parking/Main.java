package mm.parking;

import mm.parking.cli.Argument;
import mm.parking.cli.CommandLine;
import mm.parking.client.ParkingClient;
import mm.parking.parser.ParkingParser;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Main {
    private static final String ARGUMENT_DATA_TYPE = "datatype";
    private static final String ARGUMENT_FILE_FORMAT = "format";
    private static final String ARGUMENT_DIR_PATH = "dir";
    private static final String ARGUMENT_HELP = "help";

    private static final String DATA_TYPE_TARGET_PRICE = "price";
    private static final String DATA_TYPE_TARGET_WORK_HOURS = "time";
    private static final String DATA_TYPE_TARGET_LOCATION = "location";
    private static final String DATA_TYPE_TARGET_ALL = "all";

    private static final String HELP_PRINTOUT_FORMAT = "%-12s %-35s %-50s\n";

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
            }

            var fileFormats = cli.getArgumentTarget(ARGUMENT_FILE_FORMAT);
            if (fileFormats.isEmpty()) {
                System.out.printf("--%s not set, using default value.\n", ARGUMENT_FILE_FORMAT);
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

            // only one target is expected, discard others
            var target = dataTargets.get(0);

            var parkingClient = new ParkingClient();
            var parkingParser = new ParkingParser();

            switch (target) {
                case DATA_TYPE_TARGET_PRICE:
                    try {
                        System.out.println("Downloading parking price information ...");
                        var priceInfo = parkingClient.fetchParkingPrices();
                        System.out.println("Download finished.");
                        System.out.println("Parsing parking pricing information ...");
                        var prices = parkingParser.parseParkingPrices(priceInfo);
                        System.out.println("Parsing finished.");
                        System.out.println("Parsed information: " + prices);
                    } catch (IOException e) {
                        System.out.println("Failed to download parking price information: " + e.getMessage());
                        System.exit(-1);
                    }
                    break;
                case DATA_TYPE_TARGET_WORK_HOURS:
                    try {
                        System.out.println("Downloading parking work hours information ...");
                        var workHoursInfo = parkingClient.fetchParkingWorkHours();
                        System.out.println("Download finished.");
                        System.out.println("Parsing parking work hours information ...");
                        var parkingTimeData = parkingParser.parseParkingWorkHours(workHoursInfo);
                        System.out.println("Parsing finished.");
                        System.out.println("Parsed information: " + parkingTimeData);
                    } catch (IOException e) {
                        System.out.println("Failed to download parking work hours information: " + e.getMessage());
                        System.exit(-1);
                    }
                    break;
                case DATA_TYPE_TARGET_LOCATION:
                    try {
                        System.out.println("Downloading parking zones/locations information ...");
                        var locationInfo = parkingClient.fetchParkingLocations();
                        System.out.println("Download finished.");
                        System.out.println("Parsing parking locations information ...");
                        var locations = parkingParser.parseParkingLocations(locationInfo);
                        System.out.println("Parsing finished.");
                        System.out.println("Parsed information: " + locations);
                    } catch (IOException e) {
                        System.out.println("Failed to download parking zones/locations information: " + e.getMessage());
                        System.exit(-1);
                    }
                    break;
                case DATA_TYPE_TARGET_ALL:
                    System.out.println("Not supported yet ...");
                    break;
                default:
                    System.out.println("Unknown data target: " + target);
                    break;
            }
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
