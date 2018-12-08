package mm.parking.storage;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import mm.parking.ParkingLocation;
import mm.parking.ParkingPrice;
import mm.parking.ParkingTime;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class JsonStorage extends FileStorage {
    private final Gson gson;

    public JsonStorage(Gson gson, String dirPath) {
        super(dirPath);
        this.gson = gson;
    }

    @Override
    public void storePrices(List<ParkingPrice> prices) throws IOException {
        Type type = new TypeToken<List<ParkingPrice>>(){}.getType();
        write(prices, type, "prices.json");
    }

    @Override
    public void storeWorKHours(List<ParkingTime> workHours) throws IOException {
        Type type = new TypeToken<List<ParkingTime>>(){}.getType();
        write(workHours, type, "work_hours.json");
    }

    @Override
    public void storeLocations(List<ParkingLocation> locations) throws IOException {
        Type type = new TypeToken<List<ParkingLocation>>(){}.getType();
        write(locations, type, "locations.json");
    }

    private <T> void write(List<T> data, Type type, String filename) throws IOException {
        String json = gson.toJson(data, type);
        Path path = Paths.get(getDirPath() + filename);
        Files.write(path, json.getBytes());
    }
}
