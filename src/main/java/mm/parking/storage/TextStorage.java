package mm.parking.storage;

import mm.parking.ParkingLocation;
import mm.parking.ParkingPrice;
import mm.parking.ParkingTime;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class TextStorage extends FileStorage {

    public TextStorage(String dirPath) {
        super(dirPath);
    }

    @Override
    public void storePrices(List<ParkingPrice> prices, String filename) throws IOException {
        List<String> data = prices.stream()
                .map(ParkingPrice::toString)
                .collect(Collectors.toList());
        Files.write(Paths.get(getDirPath() + filename), data);
    }

    @Override
    public void storeWorkHours(List<ParkingTime> workTimeList, String filename) throws IOException {
        List<String> data = workTimeList.stream()
                .map(ParkingTime::toString)
                .collect(Collectors.toList());
        Files.write(Paths.get(getDirPath() + filename), data);
    }

    @Override
    public void storeLocations(List<ParkingLocation> locations, String filename) throws IOException {
        List<String> data = locations.stream()
                .map(ParkingLocation::toString)
                .collect(Collectors.toList());
        Files.write(Paths.get(getDirPath() + filename), data);
    }
}
