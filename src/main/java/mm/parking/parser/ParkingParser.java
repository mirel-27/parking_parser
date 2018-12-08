package mm.parking.parser;

import mm.parking.ParkingLocation;
import mm.parking.ParkingPrice;
import mm.parking.ParkingTime;

import java.util.ArrayList;
import java.util.List;

public class ParkingParser {

    public List<ParkingPrice> parseParkingPrices(String data) {
        List<ParkingPrice> prices = new ArrayList<>();
        String[] lines = tokenize(data, System.lineSeparator());

        /*
        * Data format:
        *
        * Full info -> I. zona;700101;12,00 kn/h;100,00 kn;2h;
        * Partial info 1 -> I.1. zona;/;/;150,00 kn;/;
        * Partial info 2 -> II.3. zona;700108;5,00 kn/h;60,00 kn;nije ograničeno;
        * */

        // skip the first line, it is header line
        ParkingPrice.Builder builder = ParkingPrice.Builder.newBuilder();
        for (int i = 1; i < lines.length; i++) {
            // expected token count = 5
            String[] tokens = tokenize(lines[i], ";");

            String zone = tokens[0];
            String phone = tokens[1];

            double hourlyPrice = 0;
            if (!tokens[2].equals("/")) {
                String[] hourlyPriceTokens = tokenize(tokens[2], " ");
                hourlyPrice = Double.parseDouble(hourlyPriceTokens[0].replace(',', '.'));
            }

            double dailyPrice = 0;
            if (!tokens[3].equals("/")) {
                String[] dailyPriceTokens = tokenize(tokens[3], " ");
                dailyPrice = Double.parseDouble(dailyPriceTokens[0].replace(',', '.'));
            }

            int maxHours;
            /*
               There are 2 corner cases that need to be checked for parsing max hours info:

               If zone only supports daily ticket then this value will be set to "/" and it means
               that max parking time is 24 hours. If there is no limit then the value is string
               literal = "nije ograničeno" and so we set max int value as an indicator.

               Any other case should contain the hours information in format -> e.g. 2h, 3h ... 5h etc.
           */
            if (tokens[4].equals("/")) {
                maxHours = 24;
            } else if (tokens[4].startsWith("nije")) {
                maxHours = Integer.MAX_VALUE;
            } else if (tokens[4].endsWith("h")){
                maxHours = Integer.parseInt(tokens[4].replace("h", ""));
            } else {
                maxHours = 0;
            }

            ParkingPrice parkingPrice = builder.zone(zone)
                    .phoneNumber(phone)
                    .hourlyPrice(hourlyPrice)
                    .dailyPrice(dailyPrice)
                    .maxHours(maxHours)
                    .build();

            prices.add(parkingPrice);
        }

        return prices;
    }

    public List<ParkingTime> parseParkingWorkHours(String data) {
        String[] lines = tokenize(data, System.lineSeparator());

        List<ParkingTime> workHours = new ArrayList<ParkingTime>();
        ParkingTime.Builder builder = ParkingTime.Builder.newBuilder();

        // skip first and last index -> first = header, last = garage info (not needed now)
        for (int i = 1; i < lines.length - 1; i++) {
            String[] tokens = tokenize(lines[i], ";");

            String zone = tokens[0];
            String workDayInfo = tokens[1];
            String saturdayInfo = tokens[2];
            String sundayHolidayInfo = tokens[3];

            // if parking time information for saturday/sunday equals "nema naplate" then it
            // means that there is no parking charging on saturday/sunday for that zone/region

            if (saturdayInfo.startsWith("nema")) {
                saturdayInfo = "free";
            }

            if (sundayHolidayInfo.startsWith("nema")) {
                sundayHolidayInfo = "free";
            }

            ParkingTime parkingTime = builder.zone(zone)
                    .workDayHours(workDayInfo)
                    .saturdayHours(saturdayInfo)
                    .sundayHolidayHours(sundayHolidayInfo)
                    .build();
            workHours.add(parkingTime);
        }

        return workHours;
    }

    public List<ParkingLocation> parseParkingLocations(String data) {
        String[] lines = tokenize(data, System.lineSeparator());

        List<ParkingLocation> locations = new ArrayList<ParkingLocation>();
        for (String line : lines) {
            String[] tokens = tokenize(line, ";");
            String zone = tokens[0];
            String address = tokens[1];
            String city = "Zagreb";

            // TODO: Check how to format "address" since some have additional information

            ParkingLocation parkingLocation = ParkingLocation.Builder.newBuilder()
                    .zone(zone)
                    .city(city)
                    .address(address)
                    .build();
            locations.add(parkingLocation);
        }

        return locations;
    }

    private String[] tokenize(String data, String delimiter) {
        return data.split(delimiter);
    }
}
