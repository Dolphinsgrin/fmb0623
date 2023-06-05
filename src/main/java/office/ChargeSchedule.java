package office;

import config.ConfigReader;

import java.util.List;

/***
 * Used by YAML reader to construct from file
 */
public class ChargeSchedule {
    public static final String TYPE_NOT_FOUND_MESSAGE = "type %s not found in the schedule of charges";
    private static final String FILE_NAME = "charge_schedule.yaml";
    private List<Charge> charges;

    @SuppressWarnings("unused")
    public void setCharges(List<Charge> charges) {
        this.charges = charges;
    }

    public static ChargeSchedule loadChargeSchedule() {
        return ConfigReader.readConfig(ChargeSchedule.FILE_NAME, ChargeSchedule.class);
    }

    public Charge getCharges(String type) {
        for (Charge charge : charges) {
            if (charge.getType().equals(type)) {
                return charge;
            }
        }
        throw new IllegalArgumentException(String.format(TYPE_NOT_FOUND_MESSAGE, type));
    }
}
