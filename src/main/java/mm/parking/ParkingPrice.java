package mm.parking;

import java.util.Objects;

public class ParkingPrice {
    private final String zone;
    private final String phoneNumber;
    private final double hourlyPrice;
    private final double dailyPrice;
    private final int maxHours;

    private ParkingPrice(Builder builder) {
        this.zone = builder.zone;
        this.phoneNumber = builder.phoneNumber;
        this.hourlyPrice = builder.hourlyPrice;
        this.dailyPrice = builder.dailyPrice;
        this.maxHours = builder.maxHours;
    }

    public String getZone() {
        return zone;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public double getHourlyPrice() {
        return hourlyPrice;
    }

    public double getDailyPrice() {
        return dailyPrice;
    }

    public int getMaxHours() {
        return maxHours;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParkingPrice that = (ParkingPrice) o;
        return Double.compare(that.hourlyPrice, hourlyPrice) == 0 &&
                Double.compare(that.dailyPrice, dailyPrice) == 0 &&
                maxHours == that.maxHours &&
                Objects.equals(zone, that.zone) &&
                Objects.equals(phoneNumber, that.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(zone, phoneNumber, hourlyPrice, dailyPrice, maxHours);
    }

    @Override
    public String toString() {
        return "ParkingPrice{" +
                "zone='" + zone + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", hourlyPrice=" + hourlyPrice +
                ", dailyPrice=" + dailyPrice +
                ", maxHours=" + maxHours +
                '}';
    }

    public static class Builder {
        private String zone;
        private String phoneNumber;
        private double hourlyPrice;
        private double dailyPrice;
        private int maxHours;

        private Builder() {

        }

        public static Builder newBuilder() {
            return new Builder();
        }

        public ParkingPrice build() {
            return new ParkingPrice(this);
        }

        public Builder zone(String zone) {
            this.zone = zone;
            return this;
        }

        public Builder phoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public Builder hourlyPrice(double hourlyPrice) {
            this.hourlyPrice = hourlyPrice;
            return this;
        }

        public Builder dailyPrice(double dailyPrice) {
            this.dailyPrice = dailyPrice;
            return this;
        }

        public Builder maxHours(int maxHours) {
            this.maxHours = maxHours;
            return this;
        }
    }
}
