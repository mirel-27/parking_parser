package mm.parking;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import mm.parking.cli.Argument;
import mm.parking.cli.CommandLine;
import mm.parking.client.ParkingClient;
import mm.parking.parser.ParkingParser;
import mm.parking.storage.FileStorage;
import mm.parking.storage.JsonStorage;
import mm.parking.storage.TextStorage;
import mm.parking.storage.XmlStorage;

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

    private static ParkingClient parkingClient;
    private static ParkingParser parkingParser;

    private static FileStorage xmlStorage;
    private static FileStorage jsonStorage;
    private static FileStorage textStorage;

    public static void main(String[] args) {
        List<Argument> arguments = createArguments();
        CommandLine cli = new CommandLine();

        try {
            cli.parse(args, arguments);

            if (cli.isFlagSet(ARGUMENT_HELP)) {
                printHelp();
                System.exit(0);
            }

            System.out.println("Parsing arguments ...");

            List<String> dataTargets = cli.getArgumentTarget(ARGUMENT_DATA_TYPE);
            if (dataTargets.isEmpty()) {
                System.out.printf("Required argument --%s not set.", ARGUMENT_DATA_TYPE);
                System.out.println();
                System.exit(-1);
            }

            List<String> fileFormats = cli.getArgumentTarget(ARGUMENT_FILE_FORMAT);
            if (fileFormats.isEmpty()) {
                System.out.printf("--%s not set, using default - all.\n", ARGUMENT_FILE_FORMAT);
                fileFormats.add("all");
            } else {
                for (String format : fileFormats) {
                    System.out.println("Output file format: " + format);
                }
            }

            String dirPath = "";
            List<String> directoryTarget = cli.getArgumentTarget(ARGUMENT_DIR_PATH);
            if (directoryTarget.isEmpty()) {
                System.out.printf("--%s not set, using default directory.", ARGUMENT_DIR_PATH);
                System.out.println();
            } else {
                // only one target is expected
                dirPath = directoryTarget.get(0);
                File file = new File(dirPath);
                if (!file.isDirectory()) {
                    System.out.println("Not a directory: " + dirPath);
                    System.exit(-1);
                }

                System.out.println("Output directory: " + dirPath);
            }

            System.out.println("Parsing arguments done.");

            Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .create();

            // create static objects
            parkingClient = new ParkingClient();
            parkingParser = new ParkingParser();

            xmlStorage = new XmlStorage(dirPath);
            textStorage = new TextStorage(dirPath);
            jsonStorage = new JsonStorage(dirPath, gson);

            // only one target is expected, discard others
            String target = dataTargets.get(0);
            switch (target) {
                case DATA_TYPE_TARGET_PRICE:
                    try {
                        getPrices(fileFormats);
                    } catch (IOException e) {
                        System.out.println("Error occurred" + e.getMessage());
                        System.exit(-1);
                    }
                    break;
                case DATA_TYPE_TARGET_WORK_HOURS:
                    try {
                        getWorkHours(fileFormats);
                    } catch (IOException e) {
                        System.out.println("Error occurred: " + e.getMessage());
                        System.exit(-1);
                    }
                    break;
                case DATA_TYPE_TARGET_LOCATION:
                    try {
                        getLocations(fileFormats);
                    } catch (IOException e) {
                        System.out.println("Error occurred: " + e.getMessage());
                        System.exit(-1);
                    }
                    break;
                case DATA_TYPE_TARGET_ALL:
                    try {
                        getPrices(fileFormats);
                        getWorkHours(fileFormats);
                        getLocations(fileFormats);
                    } catch (IOException e) {
                        System.out.println("Error occurred: " + e.getMessage());
                        System.exit(-1);
                    }
                    break;
                default:
                    System.out.println("Unknown data target: " + target);
                    break;
            }
        } catch (IllegalArgumentException e) {
            System.out.printf("Failed to parse arguments [%s]\n", e.getMessage());
        }
    }

    private static void getPrices(List<String> fileFormats) throws IOException {
        System.out.println("Downloading parking price information ...");
        String priceInfo = parkingClient.fetchParkingPrices();
        System.out.println("Download finished.");

        System.out.println("Parsing parking pricing information ...");
        List<ParkingPrice> prices = parkingParser.parseParkingPrices(priceInfo);
        System.out.println("Parsing finished.");
        System.out.println("Parsed information: " + prices);

        System.out.println("Writing information to disk ...");
        String filename = "prices";
        if (fileFormats.contains("all")) {
            xmlStorage.storePrices(prices, filename + ".xml");
            jsonStorage.storePrices(prices, filename + ".json");
            textStorage.storePrices(prices, filename + ".txt");
        } else {
            if (fileFormats.contains("xml")) {
                xmlStorage.storePrices(prices, filename + ".xml");
            }
            if (fileFormats.contains("json")) {
                jsonStorage.storePrices(prices, filename + ".json");
            }
            if (fileFormats.contains("raw")) {
                textStorage.storePrices(prices, filename + ".txt");
            }
        }
        System.out.println("Information written to disk.");
    }

    private static void getWorkHours(List<String> fileFormats) throws IOException {
        System.out.println("Downloading parking work hours information ...");
        String workHoursInfo = parkingClient.fetchParkingWorkHours();
        System.out.println("Download finished.");

        System.out.println("Parsing parking work hours information ...");
        List<ParkingTime> parkingTimeData = parkingParser.parseParkingWorkHours(workHoursInfo);
        System.out.println("Parsing finished.");
        System.out.println("Parsed information: " + parkingTimeData);

        System.out.println("Writing information to disk ...");
        String filename = "work_hours";
        if (fileFormats.contains("all")) {
            xmlStorage.storeWorkHours(parkingTimeData, filename + ".xml");
            jsonStorage.storeWorkHours(parkingTimeData, filename + ".json");
            textStorage.storeWorkHours(parkingTimeData, filename + ".txt");
        } else {
            if (fileFormats.contains("xml")) {
                xmlStorage.storeWorkHours(parkingTimeData, filename + ".xml");
            }
            if (fileFormats.contains("json")) {
                jsonStorage.storeWorkHours(parkingTimeData, filename + ".json");
            }
            if (fileFormats.contains("raw")) {
                textStorage.storeWorkHours(parkingTimeData, filename + ".txt");
            }
        }
        System.out.println("Information written to disk.");
    }

    private static void getLocations(List<String> fileFormats) throws IOException {
        System.out.println("Downloading parking zones/locations information ...");
        String locationInfo = parkingClient.fetchParkingLocations();
        System.out.println("Download finished.");

        System.out.println("Parsing parking locations information ...");
        List<ParkingLocation> locations = parkingParser.parseParkingLocations(locationInfo);
        System.out.println("Parsing finished.");
        System.out.println("Parsed information: " + locations);

        System.out.println("Writing information to disk ...");
        String filename = "locations";
        if (fileFormats.contains("all")) {
            xmlStorage.storeLocations(locations, filename + ".xml");
            jsonStorage.storeLocations(locations, filename + ".json");
            textStorage.storeLocations(locations, filename + ".txt");
        } else {
            if (fileFormats.contains("xml")) {
                xmlStorage.storeLocations(locations, filename + ".xml");
            }
            if (fileFormats.contains("json")) {
                jsonStorage.storeLocations(locations, filename + ".json");
            }
            if (fileFormats.contains("raw")) {
                textStorage.storeLocations(locations, filename + ".txt");
            }
        }
        System.out.println("Information written to disk.");
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
        String arg = "Argument";
        String values = "Values";
        String description = "Description";
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
