package office;

import java.util.List;

public class ChargeSchedule {
    public static final String TYPE_NOT_FOUND_MESSAGE = "type %s not found in the schedule of charges";
    public static final String FILE_NAME = "charge_schedule.yaml";
    private List<Charges> schedule;

    public void setSchedule(List<Charges> schedule) {
        this.schedule = schedule;
    }

    public Charges getCharges(String type) {
        for (Charges charges : schedule) {
            if (charges.getType().equals(type)) {
                return charges;
            }
        }
        throw new IllegalArgumentException(String.format(TYPE_NOT_FOUND_MESSAGE, type));
    }
}
