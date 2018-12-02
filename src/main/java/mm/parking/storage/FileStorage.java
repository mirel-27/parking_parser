package mm.parking.storage;

import mm.parking.ParkingLocation;
import mm.parking.ParkingPrice;
import mm.parking.ParkingTime;

import java.io.IOException;
import java.util.List;

public abstract class FileStorage {
    private final String dirPath;

    public FileStorage(String dirPath) {
        this.dirPath = dirPath;
    }

    public String getDirPath() {
        return dirPath;
    }

    public abstract void storePrices(List<ParkingPrice> prices) throws IOException;

    public abstract void storeWorKHours(List<ParkingTime> workTimeList) throws IOException;

    public abstract void storeLocations(List<ParkingLocation> locations) throws IOException;
}
