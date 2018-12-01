package mm.parking;

import java.util.Objects;

public class ParkingTime {
    private final String zone;
    private final String workDayHours;
    private final String saturdayHours;
    private final String sundayHolidayHours;

    private ParkingTime(Builder builder) {
        this.zone = builder.zone;
        this.workDayHours = builder.workDayHours;
        this.saturdayHours = builder.saturdayHours;
        this.sundayHolidayHours = builder.sundayHolidayHours;
    }

    public String getZone() {
        return zone;
    }

    public String getWorkDayHours() {
        return workDayHours;
    }

    public String getSaturdayHours() {
        return saturdayHours;
    }

    public String getSundayHolidayHours() {
        return sundayHolidayHours;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParkingTime that = (ParkingTime) o;
        return Objects.equals(zone, that.zone) &&
                Objects.equals(workDayHours, that.workDayHours) &&
                Objects.equals(saturdayHours, that.saturdayHours) &&
                Objects.equals(sundayHolidayHours, that.sundayHolidayHours);
    }

    @Override
    public int hashCode() {
        return Objects.hash(zone, workDayHours, saturdayHours, sundayHolidayHours);
    }

    @Override
    public String toString() {
        return "ParkingTime{" +
                "zone='" + zone + '\'' +
                ", workDayHours='" + workDayHours + '\'' +
                ", saturdayHours='" + saturdayHours + '\'' +
                ", sundayHolidayHours='" + sundayHolidayHours + '\'' +
                '}';
    }

    public static class Builder {
        private String zone;
        private String workDayHours;
        private String saturdayHours;
        private String sundayHolidayHours;

        private Builder() {

        }

        public static Builder newBuilder() {
            return new Builder();
        }

        public ParkingTime build() {
            return new ParkingTime(this);
        }

        public Builder zone(String zone) {
            this.zone = zone;
            return this;
        }

        public Builder workDayHours(String workDayHours) {
            this.workDayHours = workDayHours;
            return this;
        }

        public Builder saturdayHours(String saturdayHours) {
            this.saturdayHours = saturdayHours;
            return this;
        }

        public Builder sundayHolidayHours(String sundayHolidayHours) {
            this.sundayHolidayHours = sundayHolidayHours;
            return this;
        }
    }
}
