package office;

import shed.Tool;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class RentalAgreement {
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

    public RentalAgreement(Tool tool, int rentalDays, LocalDate checkoutDate, int discountPercent) {
        this.tool = tool;
        this.rentalDays = rentalDays;
        this.checkoutDate = checkoutDate;
        this.discountPercent = discountPercent;
        crunchNumbers();
    }

    /***
     * The sequencing of the below actions are important. For example, attempting to calculate pre-discount charge
     * before calculating the charge days or the daily charge rate will result in bad data.
     * TODO: group appropriately
     */
    private void crunchNumbers() {
        calcDueDate();
        lookupRentalCharge();
        calcDaysCharged();
        calcPrediscountCharge();
        calcDiscountAmount();
        calcFinalCharge();
    }

    private void calcDueDate() {
        dueDate = checkoutDate.plusDays(rentalDays);
    }

    private void lookupRentalCharge() {
        // TODO: implement
        dailyRentalCharge = 2;
    }

    private void calcDaysCharged() {
        // TODO: implement date screening
        daysCharged = rentalDays;
    }

    private void calcPrediscountCharge() {
        prediscountCharge = daysCharged * dailyRentalCharge;
    }

    private void calcDiscountAmount() {
        discountAmount = prediscountCharge * (double)discountPercent / 100;
    }

    private void calcFinalCharge() {
        finalCharge = prediscountCharge - discountAmount;
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
        NumberFormat numberFormatter = NumberFormat.getCurrencyInstance();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yy");
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
