package office;

import config.Common;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import static config.Common.numberFormatter;

public class Checkout {
    public static final String BAD_DISCOUNT_PERCENTAGE_MESSAGE = "the requested discount percentage (%d) must be between 0 and 100";
    public static final String BAD_RENTAL_DAYS_MESSAGE = "the number of requested rental days (%d) must be greater than 0";

    // independent values
    private final Tool tool;
    private final int rentalDays;
    private final LocalDate checkoutDate;
    private final int discountPercent;

    // calculated/evaluated values
    private LocalDate dueDate;
    private BigDecimal dailyRentalCharge;
    private long calculatedDaysCharged;
    private BigDecimal preDiscountCharge;
    private BigDecimal discountAmount;
    private BigDecimal finalCharge;

    public Checkout(Tool tool, int rentalDays, LocalDate checkoutDate, int discountPercent) throws IllegalArgumentException {
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
        // add the number of rental days to the checkout date to get dueDate
        dueDate = checkoutDate.plusDays(rentalDays);
        Charge toolCharge = ChargeSchedule.getChargeForType(tool.getType());
        dailyRentalCharge = BigDecimal.valueOf(toolCharge.getDailyCharge());
        calculatedDaysCharged = CalendarEvaluator.calculateNumOfDaysToCharge(checkoutDate, rentalDays, toolCharge.isWeekendCharged(), toolCharge.isHolidayCharged());
        preDiscountCharge = dailyRentalCharge.multiply(BigDecimal.valueOf(calculatedDaysCharged));
        discountAmount = preDiscountCharge.multiply(BigDecimal.valueOf(discountPercent)).divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP);
        finalCharge = preDiscountCharge.subtract(discountAmount);
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

    public BigDecimal getDailyRentalCharge() {
        return dailyRentalCharge;
    }

    public long getCalculatedDaysCharged() {
        return calculatedDaysCharged;
    }

    public BigDecimal getPreDiscountCharge() {
        return preDiscountCharge;
    }

    public int getDiscountPercent() {
        return discountPercent;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public BigDecimal getFinalCharge() {
        return finalCharge;
    }

    public String printRentalAgreement() {
        String sb = "************************************\n" +
                "Tool code: " + tool.getCode() + "\n" +
                "Tool type: " + tool.getType() + "\n" +
                "Tool brand: " + tool.getBrand() + "\n" +
                "Rental days: " + rentalDays + "\n" +
                "Check out date: " + Common.dateFormatter.format(checkoutDate) + "\n" +
                "Due date: " + Common.dateFormatter.format(dueDate) + "\n" +
                "Daily rental charge: " + numberFormatter.format(dailyRentalCharge) + "\n" +
                "Charge days: " + calculatedDaysCharged + "\n" +
                "Pre-discount charge: " + numberFormatter.format(preDiscountCharge) + "\n" +
                "Discount percent: " + discountPercent + "%" + "\n" +
                "Discount amount: " + numberFormatter.format(discountAmount) + "\n" +
                "Final charge: " + numberFormatter.format(finalCharge);
        System.out.println(sb);
        return sb;
    }
}
