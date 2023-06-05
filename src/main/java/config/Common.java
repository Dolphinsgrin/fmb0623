package config;

import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;

public class Common {
    public static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yy");
    public static final NumberFormat numberFormatter = NumberFormat.getCurrencyInstance();
    public static final String definedExitCode = "exit";
    public static final String farewellMessage = "Thank you for using the Tool Rental App. Have a great day!";
}
