package office;

import config.ConfigReader;
import org.junit.jupiter.api.Test;

class ChargeScheduleTest {

    @Test
    void testReader() {
        ChargeSchedule underTest = ConfigReader.readConfig(ChargeSchedule.FILE_NAME, ChargeSchedule.class);
        System.out.println(underTest);
    }

}