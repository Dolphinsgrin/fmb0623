package config;

import office.ChargeSchedule;

import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;

public class Common {
    public static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yy");
    public static final NumberFormat numberFormatter = NumberFormat.getCurrencyInstance();
    public static final String definedExitCode = "exit";
    public static final ChargeSchedule chargeSchedule = ChargeSchedule.loadChargeSchedule();
}
