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
}
