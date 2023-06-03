package office;

import config.ConfigReader;
import shed.Tool;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class RentalAgreement {
    public static final String BAD_DISCOUNT_PERCENTAGE_MESSAGE = "the requested discount percentage (%d%%) must be between 0%% and 100%%";
    public static final String BAD_RENTAL_DAYS_MESSAGE = "the number of requested rental days (%d) must be greater than 0";
    private final Tool tool;
    private final int rentalDays;
    private final LocalDate checkoutDate;
    private final int discountPercent;

    // calculated/evaluated values
    private LocalDate dueDate;
    private double dailyRentalCharge;
    private long daysCharged;
    private double prediscountCharge;
    private double discountAmount;
    private double finalCharge;

    private static final ChargeSchedule chargeSchedule = ConfigReader.readConfig(ChargeSchedule.FILE_NAME, ChargeSchedule.class);
    private static final NumberFormat numberFormatter = NumberFormat.getCurrencyInstance();
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yy");

    public RentalAgreement(Tool tool, int rentalDays, LocalDate checkoutDate, int discountPercent) throws IllegalArgumentException {
        validateInputs(rentalDays, discountPercent);
        this.tool = tool;
        this.rentalDays = rentalDays;
        this.checkoutDate = checkoutDate;
        this.discountPercent = discountPercent;
        init();
    }

    private void validateInputs(int rentalDays, int discountPercent) throws IllegalArgumentException {
        if (rentalDays < 1) {
            throw new IllegalArgumentException(String.format(BAD_RENTAL_DAYS_MESSAGE, rentalDays));
        }
        if (discountPercent < 0 || discountPercent > 100) {
            throw new IllegalArgumentException(String.format(BAD_DISCOUNT_PERCENTAGE_MESSAGE, discountPercent));
        }
    }

    /***
     * The sequencing of the below actions are important. For example, attempting to calculate pre-discount charge
     * before calculating the charge days or the daily charge rate will result in bad data.
     */
    private void init() {
        dueDate = checkoutDate.plusDays(rentalDays);
        dailyRentalCharge = chargeSchedule.getCharges(tool.getType()).getDaily_charge();
        calcDaysCharged();
        prediscountCharge = daysCharged * dailyRentalCharge;
        discountAmount = roundToTwoDecimal(prediscountCharge * (double)discountPercent / 100);
        finalCharge = roundToTwoDecimal(prediscountCharge - discountAmount);
    }

    private double roundToTwoDecimal(double value) {
        // TODO: verify this rounds the half up
        return (double) Math.round(value * 100.0) / 100.0;
    }

    private void calcDaysCharged() {
        // TODO: implement date screening
        daysCharged = rentalDays;
    }

    public Tool getTool() {
        return tool;
    }

    public int getRentalDays() {
        return rentalDays;
    }

    public LocalDate getCheckoutDate() {
        return checkoutDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public double getDailyRentalCharge() {
        return dailyRentalCharge;
    }

    public long getDaysCharged() {
        return daysCharged;
    }

    public double getPrediscountCharge() {
        return prediscountCharge;
    }

    public int getDiscountPercent() {
        return discountPercent;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }

    public double getFinalCharge() {
        return finalCharge;
    }

    public void printAgreement() {
        String sb = "Tool code: " + tool.getCode() + "\n" +
                "Tool type: " + tool.getType() + "\n" +
                "Tool brand: " + tool.getBrand() + "\n" +
                "Rental days: " + rentalDays + "\n" +
                "Check out date: " + dateFormatter.format(checkoutDate) + "\n" +
                "Due date: " + dateFormatter.format(dueDate) + "\n" +
                "Daily rental charge: " + numberFormatter.format(dailyRentalCharge) + "\n" +
                "Charge days: " + daysCharged + "\n" +
                "Pre-discount charge: " + numberFormatter.format(prediscountCharge) + "\n" +
                "Discount percent: " + discountPercent + "%" + "\n" +
                "Discount amount: " + numberFormatter.format(discountAmount) + "\n" +
                "Final charge: " + numberFormatter.format(finalCharge);
        System.out.println(sb);
    }
}
