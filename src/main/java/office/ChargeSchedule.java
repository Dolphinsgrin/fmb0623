package office;

import config.ConfigReader;

import java.io.IOException;
import java.util.List;

/***
 * Used by YAML reader to construct from file
 */
public class ChargeSchedule {
    public static final String TYPE_NOT_FOUND_MESSAGE = "type %s not found in the schedule of charges";
    private static final String FILE_NAME = "charge_schedule.yaml";
    private List<Charge> charges;
    private static ChargeSchedule chargeSchedule;

    @SuppressWarnings("unused")
    public void setCharges(List<Charge> charges) {
        this.charges = charges;
    }

    private static ChargeSchedule loadChargeSchedule() throws IOException {
        if (chargeSchedule == null) {
            chargeSchedule = ConfigReader.readConfig(ChargeSchedule.FILE_NAME, ChargeSchedule.class);
        }
        return chargeSchedule;
    }

    public static Charge getChargeForType(String type) throws IOException {
        for (Charge charge : loadChargeSchedule().charges) {
            if (charge.getType().equals(type)) {
                return charge;
            }
        }
        throw new IllegalArgumentException(String.format(TYPE_NOT_FOUND_MESSAGE, type));
    }

    /***
     * Used by YAML reader to construct, suppressing 'unused'
     */
    public static class Charge {
        private String type;
        private double daily_charge;
        private boolean charge_weekdays;
        private boolean charge_weekends;
        private boolean charge_holidays;

        public String getType() {
            return type;
        }

        @SuppressWarnings("unused")
        public void setType(String type) {
            this.type = type;
        }

        public double getDailyCharge() {
            return daily_charge;
        }

        @SuppressWarnings("unused")
        public void setDaily_charge(double daily_charge) {
            this.daily_charge = daily_charge;
        }

        @SuppressWarnings("unused")
        public boolean isCharge_weekdays() {
            return charge_weekdays;
        }

        @SuppressWarnings("unused")
        public void setCharge_weekdays(boolean charge_weekdays) {
            this.charge_weekdays = charge_weekdays;
        }

        public boolean isWeekendCharged() {
            return charge_weekends;
        }

        @SuppressWarnings("unused")
        public void setCharge_weekends(boolean charge_weekends) {
            this.charge_weekends = charge_weekends;
        }

        public boolean isHolidayCharged() {
            return charge_holidays;
        }

        @SuppressWarnings("unused")
        public void setCharge_holidays(boolean charge_holidays) {
            this.charge_holidays = charge_holidays;
        }
    }
}
