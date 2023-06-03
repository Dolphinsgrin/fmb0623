package office;

public class Charges {
        private String type;
        private double daily_charge;
        private boolean charge_weekdays;
        private boolean charge_weekends;
        private boolean charge_holidays;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public double getDaily_charge() {
            return daily_charge;
        }

        public void setDaily_charge(double daily_charge) {
            this.daily_charge = daily_charge;
        }

        public boolean isCharge_weekdays() {
            return charge_weekdays;
        }

        public void setCharge_weekdays(boolean charge_weekdays) {
            this.charge_weekdays = charge_weekdays;
        }

        public boolean isCharge_weekends() {
            return charge_weekends;
        }

        public void setCharge_weekends(boolean charge_weekends) {
            this.charge_weekends = charge_weekends;
        }

        public boolean isCharge_holidays() {
            return charge_holidays;
        }

        public void setCharge_holidays(boolean charge_holidays) {
            this.charge_holidays = charge_holidays;
        }
}
