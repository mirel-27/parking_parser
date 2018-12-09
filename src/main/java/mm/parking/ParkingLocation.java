package mm.parking;

import java.util.Objects;

public class ParkingLocation {
    private final String zone;
    private final String city;
    private final String address;

    private ParkingLocation(Builder builder) {
        this.zone = builder.zone;
        this.city = builder.city;
        this.address = builder.address;
    }

    public String getZone() {
        return zone;
    }

    public String getCity() {
        return city;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParkingLocation that = (ParkingLocation) o;
        return Objects.equals(zone, that.zone) &&
                Objects.equals(city, that.city) &&
                Objects.equals(address, that.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(zone, city, address);
    }

    @Override
    public String toString() {
        return "zone=" + zone + " " +
                "city=" + city + " " +
                "address=" + address;
    }

    public static class Builder {
        private String zone;
        private String city;
        private String address;

        private Builder() {

        }

        public static Builder newBuilder() {
            return new Builder();
        }

        public ParkingLocation build() {
            return new ParkingLocation(this);
        }

        public Builder zone(String zone) {
            this.zone = zone;
            return this;
        }

        public Builder city(String city) {
            this.city = city;
            return this;
        }

        public Builder address(String address) {
            this.address = address;
            return this;
        }
    }
}
