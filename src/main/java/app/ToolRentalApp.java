package app;

import office.Checkout;
import office.Tools;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

import static config.Common.dateFormatter;
import static config.Common.definedExitCode;
import static config.Common.farewellMessage;

public class ToolRentalApp {
    @SuppressWarnings("InfiniteLoopStatement")
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.println("*** New rental application (type 'exit' to exit) ***");
                String code;
                int numOfDays;
                int discountPercentage;
                LocalDate checkoutDate;
                // passing messages even when only called once for readability here (re:SuppressWarnings("SameParameterValue"))
                code = queryForToolCode(scanner, "Enter tool code: ");
                numOfDays = queryForInt(scanner, "Enter number of days the tool will be rented: ", 1, -1);
                discountPercentage = queryForInt(scanner, "Enter discount percentage (as whole number): ", 0, 100);
                checkoutDate = queryForCheckoutDate(scanner, "Enter checkout date (mm/dd/yy): ");
                Checkout ra = new Checkout(Tools.getTool(code), numOfDays, checkoutDate, discountPercentage);
                ra.printRentalAgreement();
                System.out.println();
            }
        } catch (IOException e) {
            System.out.println("Error: unable to load data file(s), unable to continue.");
            System.exit(-1);
        }
    }

    private static void checkExit(String exitCode) {
        if (definedExitCode.equals(exitCode)) {
            System.out.println(farewellMessage);
            System.exit(0);
        }
    }

    @SuppressWarnings("SameParameterValue")
    private static LocalDate queryForCheckoutDate(Scanner scanner, String message) {
        LocalDate date = null;
        while (date == null) {
            System.out.print(message);
            String entry = scanner.nextLine();
            checkExit(entry);
            try {
                date = LocalDate.parse(entry, dateFormatter);
            } catch (DateTimeParseException e) {
                System.out.println("INVALID DATE ENTRY: Example usage - 01/31/20");
            }
        }
        return date;
    }

    @SuppressWarnings("SameParameterValue")
    private static String queryForToolCode(Scanner scanner, String message) throws IOException {
        String code = null;
        while (code == null) {
            System.out.print(message);
            code = scanner.nextLine();
            checkExit(code);
            try {
                Tools.getTool(code);
            } catch (IllegalArgumentException e) {
                System.out.println("INVALID TOOL CODE, PLEASE TRY AGAIN");
                code = null;
            }
        }
        return code;
    }

    private static int queryForInt(Scanner scanner, String message, int min, int max) {
        int number = -1;
        while (number == -1) {
            System.out.print(message);
            try {
                String entry = scanner.nextLine();
                checkExit(entry);
                number = Integer.parseInt(entry);
            } catch (NumberFormatException e) {
                System.out.println("INVALID NUMBER FORMAT, PLEASE TRY AGAIN");
                continue;
            }
            if (number < min) {
                System.out.printf("INVALID ENTRY: number must be greater than %d%n", min);
                number = -1;
            } else if (max != -1 && number > max) {
                System.out.printf("INVALID ENTRY: number must be less than %d%n", max);
                number = -1;
            }
        }
        return number;
    }
}
