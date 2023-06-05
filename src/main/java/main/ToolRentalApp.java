package main;

import office.RentalAgreement;
import office.Shed;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

import static config.Common.dateFormatter;
import static config.Common.definedExitCode;

public class ToolRentalApp {
    private static final Shed shed = Shed.loadShed();
    public static void main(String[] args) {
        ToolRentalApp app = new ToolRentalApp();
        app.queryInputs();
    }

    @SuppressWarnings("InfiniteLoopStatement")
    private void queryInputs() {
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.println("*** New rental application (type 'exit' to exit) ***");
                String code;
                int numOfDays;
                int discountPercentage;
                LocalDate checkoutDate;
                code = queryForToolCode(scanner);
                numOfDays = queryForInt(scanner, "Enter number of days the tool will be rented: ", 1, -1);
                discountPercentage = queryForInt(scanner, "Enter discount percentage (as whole number): ", 0, 100);
                checkoutDate = queryForCheckoutDate(scanner);
                RentalAgreement ra = new RentalAgreement(shed.getTool(code), numOfDays, checkoutDate, discountPercentage);
                ra.printAgreement();
                System.out.println();
            }
        }
    }

    private void checkExit(String exitCode) {
        if (definedExitCode.equals(exitCode)) {
            System.out.println("Thank you for using the Tool Rental App. Have a great day!");
            System.exit(0);
        }
    }

    private LocalDate queryForCheckoutDate(Scanner scanner) {
        LocalDate date;
        do {
            System.out.print("Enter checkout date (mm/dd/yy): ");
            String entry = scanner.nextLine();
            checkExit(entry);
            try {
                date = LocalDate.parse(entry, dateFormatter);
            } catch (DateTimeParseException e) {
                System.out.println("INVALID DATE ENTRY: Example usage - 01/31/20");
                date = null;
            }
        } while (date == null);

        return date;
    }

    private String queryForToolCode(Scanner scanner) {
        String code;
        do {
            System.out.print("Enter tool code: ");
            code = scanner.nextLine();
            checkExit(code);
            try {
                shed.getTool(code);
            } catch (IllegalArgumentException e) {
                System.out.println("INVALID TOOL CODE, PLEASE TRY AGAIN");
                code = null;
            }
        } while (code == null);
        return code;
    }

    private int queryForInt(Scanner scanner, String message, int min, int max) {
        int number = -1;
        do {
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
            }
            if (max != -1 && number > max) {
                System.out.printf("INVALID ENTRY: number must be less than %d%n", max);
                number = -1;
            }
        } while (number == -1);
        return number;
    }
}
