package office;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static config.Common.dateFormatter;
import static config.Common.numberFormatter;
import static office.Checkout.BAD_DISCOUNT_PERCENTAGE_MESSAGE;
import static office.Checkout.BAD_RENTAL_DAYS_MESSAGE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CheckoutTest {
    @Test
    void test_number_formatter_uses_commas() {
        BigDecimal fourDigitNumber = BigDecimal.valueOf(9999.99D);
        assertEquals("$9,999.99", numberFormatter.format(fourDigitNumber));
    }

    @Test
    void test_dateFormatter_mm_dd_yy() {
        LocalDate testDate = LocalDate.parse("01/31/23", dateFormatter);
        assertEquals("01/31/23", testDate.format(dateFormatter));
    }

    @Test
    void test_challenge_proof_test1a_bad_discount() {
        int badDiscountValue = 101;
        String expectedErrorMessage = String.format(BAD_DISCOUNT_PERCENTAGE_MESSAGE, badDiscountValue);
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> new Checkout(Shed.getTool("JAKR"), 5, LocalDate.parse("09/03/15", dateFormatter), badDiscountValue));
        assertEquals(e.getMessage(), expectedErrorMessage);
    }

    @Test
    void test_challenge_proof_test1b_bad_rental_days() {
        int badRentalDays = -3;
        String expectedErrorMessage = String.format(BAD_RENTAL_DAYS_MESSAGE, badRentalDays);
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> new Checkout(Shed.getTool("JAKR"), badRentalDays, LocalDate.parse("09/03/15", dateFormatter), 0));
        assertEquals(e.getMessage(), expectedErrorMessage);
    }

    @Test
    void test_challenge_proof_test2() throws Exception {
        // test values
        String code = "LADW";
        int rentalDays = 3;
        String checkoutDayString = "07/02/20";
        int discountPercentage = 10;

        // explicitly build expected outputs
        Tool expectedTool = new Tool();
        expectedTool.setCode(code);
        expectedTool.setBrand("Werner");
        expectedTool.setType("Ladder");
        StaticValues expected = new StaticValues(
                expectedTool,
                rentalDays,
                LocalDate.parse(checkoutDayString, dateFormatter),
                discountPercentage,
                LocalDate.parse("07/05/20", dateFormatter),
                new BigDecimal("1.99"),
                3, // July 4th falls on a Saturday, => not observed until Monday the 6th.
                new BigDecimal("5.97"),
                new BigDecimal(".60"),
                new BigDecimal("5.37")
        );
        // generate the rental agreement
        Checkout underTest = new Checkout(Shed.getTool(code), rentalDays, LocalDate.parse(checkoutDayString, dateFormatter), discountPercentage);
        // test the results
        validateScenario(underTest, expected);
        assertEquals("""
                ************************************
                Tool code: LADW
                Tool type: Ladder
                Tool brand: Werner
                Rental days: 3
                Check out date: 07/02/20
                Due date: 07/05/20
                Daily rental charge: $1.99
                Charge days: 3
                Pre-discount charge: $5.97
                Discount percent: 10%
                Discount amount: $0.60
                Final charge: $5.37""", underTest.printRentalAgreement());
    }

    @Test
    void test_challenge_proof_test3() throws Exception {
        // test values
        String code = "CHNS";
        int rentalDays = 5;
        String checkoutDayString = "07/02/15";
        int discountPercentage = 25;

        // explicitly build expected outputs
        Tool expectedTool = new Tool();
        expectedTool.setCode(code);
        expectedTool.setBrand("Stihl");
        expectedTool.setType("Chainsaw");
        StaticValues expected = new StaticValues(
                expectedTool,
                rentalDays,
                LocalDate.parse(checkoutDayString, dateFormatter),
                discountPercentage,
                LocalDate.parse("07/07/15", dateFormatter),
                new BigDecimal("1.49"),
                3, // charge the weekend, but not the Monday
                new BigDecimal("4.47"),
                new BigDecimal("1.12"),
                new BigDecimal("3.35")
        );
        // generate the rental agreement
        Checkout underTest = new Checkout(Shed.getTool(code), rentalDays, LocalDate.parse(checkoutDayString, dateFormatter), discountPercentage);
        // test the results
        validateScenario(underTest, expected);
        assertEquals("""
                ************************************
                Tool code: CHNS
                Tool type: Chainsaw
                Tool brand: Stihl
                Rental days: 5
                Check out date: 07/02/15
                Due date: 07/07/15
                Daily rental charge: $1.49
                Charge days: 3
                Pre-discount charge: $4.47
                Discount percent: 25%
                Discount amount: $1.12
                Final charge: $3.35""", underTest.printRentalAgreement());
    }

    @Test
    void test_challenge_proof_test4() throws Exception {
        // test values
        String code = "JAKD";
        int rentalDays = 6;
        String checkoutDayString = "09/03/15";
        int discountPercentage = 0;

        // explicitly build expected outputs
        Tool expectedTool = new Tool();
        expectedTool.setCode(code);
        expectedTool.setBrand("DeWalt");
        expectedTool.setType("Jackhammer");
        StaticValues expected = new StaticValues(
                expectedTool,
                rentalDays,
                LocalDate.parse(checkoutDayString, dateFormatter),
                discountPercentage,
                LocalDate.parse("09/09/15", dateFormatter),
                new BigDecimal("2.99"),
                3, // no charge over weekend, no charge on Monday (Labor day)
                new BigDecimal("8.97"),
                new BigDecimal("0.00"),
                new BigDecimal("8.97")
        );
        // generate the rental agreement
        Checkout underTest = new Checkout(Shed.getTool(code), rentalDays, LocalDate.parse(checkoutDayString, dateFormatter), discountPercentage);
        // test the results
        validateScenario(underTest, expected);
        assertEquals("""
                ************************************
                Tool code: JAKD
                Tool type: Jackhammer
                Tool brand: DeWalt
                Rental days: 6
                Check out date: 09/03/15
                Due date: 09/09/15
                Daily rental charge: $2.99
                Charge days: 3
                Pre-discount charge: $8.97
                Discount percent: 0%
                Discount amount: $0.00
                Final charge: $8.97""", underTest.printRentalAgreement());
    }

    @Test
    void test_challenge_proof_test5() throws Exception {
        // test values
        String code = "JAKR";
        int rentalDays = 9;
        String checkoutDayString = "07/02/15";
        int discountPercentage = 0;

        // explicitly build expected outputs
        Tool expectedTool = new Tool();
        expectedTool.setCode(code);
        expectedTool.setBrand("Ridgid");
        expectedTool.setType("Jackhammer");
        StaticValues expected = new StaticValues(
                expectedTool,
                rentalDays,
                LocalDate.parse(checkoutDayString, dateFormatter),
                discountPercentage,
                LocalDate.parse("07/11/15", dateFormatter),
                new BigDecimal("2.99"),
                5, // no charge over weekend, no charge on Monday (Labor day)
                new BigDecimal("14.95"),
                new BigDecimal("0.00"),
                new BigDecimal("14.95")
        );
        // generate the rental agreement
        Checkout underTest = new Checkout(Shed.getTool(code), rentalDays, LocalDate.parse(checkoutDayString, dateFormatter), discountPercentage);
        // test the results
        validateScenario(underTest, expected);
        assertEquals("""
                ************************************
                Tool code: JAKR
                Tool type: Jackhammer
                Tool brand: Ridgid
                Rental days: 9
                Check out date: 07/02/15
                Due date: 07/11/15
                Daily rental charge: $2.99
                Charge days: 5
                Pre-discount charge: $14.95
                Discount percent: 0%
                Discount amount: $0.00
                Final charge: $14.95""", underTest.printRentalAgreement());
    }

    @Test
    void test_challenge_proof_test6() throws Exception {
        // test values
        String code = "JAKR";
        int rentalDays = 4;
        String checkoutDayString = "07/02/20";
        int discountPercentage = 50;

        // explicitly build expected outputs
        Tool expectedTool = new Tool();
        expectedTool.setCode(code);
        expectedTool.setBrand("Ridgid");
        expectedTool.setType("Jackhammer");
        StaticValues expected = new StaticValues(
                expectedTool,
                rentalDays,
                LocalDate.parse(checkoutDayString, dateFormatter),
                discountPercentage,
                LocalDate.parse("07/06/20", dateFormatter),
                new BigDecimal("2.99"),
                1, // no charge over weekend, no charge on Monday (deferred 4th of July)
                new BigDecimal("2.99"),
                new BigDecimal("1.50"), // 1.495 rounding up!
                new BigDecimal("1.49")
        );
        // generate the rental agreement
        Checkout underTest = new Checkout(Shed.getTool(code), rentalDays, LocalDate.parse(checkoutDayString, dateFormatter), discountPercentage);
        // test the results
        validateScenario(underTest, expected);
        assertEquals("""
                ************************************
                Tool code: JAKR
                Tool type: Jackhammer
                Tool brand: Ridgid
                Rental days: 4
                Check out date: 07/02/20
                Due date: 07/06/20
                Daily rental charge: $2.99
                Charge days: 1
                Pre-discount charge: $2.99
                Discount percent: 50%
                Discount amount: $1.50
                Final charge: $1.49""", underTest.printRentalAgreement());
    }

    @Test
    void test_rental_starts_after_weekend_4th() {
        // test values
        String code = "JAKR";
        int rentalDays = 3;
        String checkoutDayString = "07/05/20";
        int discountPercentage = 50;

        // explicitly build expected outputs
        Tool expectedTool = new Tool();
        expectedTool.setCode(code);
        expectedTool.setBrand("Ridgid");
        expectedTool.setType("Jackhammer");
        StaticValues expected = new StaticValues(
                expectedTool,
                rentalDays,
                LocalDate.parse(checkoutDayString, dateFormatter),
                discountPercentage,
                LocalDate.parse("07/08/20", dateFormatter),
                new BigDecimal("2.99"),
                2, // no charge on Monday (deferred 4th of July)
                new BigDecimal("5.98"),
                new BigDecimal("2.99"), // 1.495 rounding up!
                new BigDecimal("2.99")
        );
        // generate the rental agreement
        Checkout underTest = new Checkout(Shed.getTool(code), rentalDays, LocalDate.parse(checkoutDayString, dateFormatter), discountPercentage);
        // test the results
        validateScenario(underTest, expected);
        assertEquals("""
                ************************************
                Tool code: JAKR
                Tool type: Jackhammer
                Tool brand: Ridgid
                Rental days: 3
                Check out date: 07/05/20
                Due date: 07/08/20
                Daily rental charge: $2.99
                Charge days: 2
                Pre-discount charge: $5.98
                Discount percent: 50%
                Discount amount: $2.99
                Final charge: $2.99""", underTest.printRentalAgreement());
    }

    void validateScenario(Checkout input, StaticValues expected) {
        assertEquals(expected.getTool().getCode(), input.getTool().getCode(), "failed code");
        assertEquals(expected.getTool().getType(), input.getTool().getType(), "failed type");
        assertEquals(expected.getTool().getBrand(), input.getTool().getBrand(), "failed brand");
        assertEquals(expected.getRentalDays(), input.getRentalDays(), "failed rental days");
        assertEquals(expected.getCheckoutDate(), input.getCheckoutDate(), "failed checkout date");
        assertEquals(expected.getDiscountPercent(), input.getDiscountPercent(), "failed discount percent");
        assertEquals(expected.getDueDate(), input.getDueDate(), "failed due date");
        assertEquals(expected.getDailyRentalCharge(), input.getDailyRentalCharge(), "failed daily rental charge");
        assertEquals(expected.getDaysCharged(), input.getCalculatedDaysCharged(), "failed days charged");
        assertEquals(expected.getPrediscountCharge(), input.getPreDiscountCharge(), "failed pre-discount charge");
        assertEquals(expected.getDiscountAmount(), input.getDiscountAmount(), "failed discount amount");
        assertEquals(expected.getFinalCharge(), input.getFinalCharge(), "failed final charge");
    }

    private static class StaticValues {
        private final Tool tool;
        private final int rentalDays;
        private final LocalDate checkoutDate;
        private final int discountPercent;
        private final LocalDate dueDate;
        private final BigDecimal dailyRentalCharge;
        private final long daysCharged;
        private final BigDecimal prediscountCharge;
        private final BigDecimal discountAmount;
        private final BigDecimal finalCharge;

        public StaticValues(Tool tool, int rentalDays, LocalDate checkoutDate, int discountPercent, LocalDate dueDate, BigDecimal dailyRentalCharge, long daysCharged, BigDecimal prediscountCharge, BigDecimal discountAmount, BigDecimal finalCharge) {
            this.tool = tool;
            this.rentalDays = rentalDays;
            this.checkoutDate = checkoutDate;
            this.discountPercent = discountPercent;
            this.dueDate = dueDate;
            this.dailyRentalCharge = dailyRentalCharge;
            this.daysCharged = daysCharged;
            this.prediscountCharge = prediscountCharge;
            this.discountAmount = discountAmount;
            this.finalCharge = finalCharge;
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

        public int getDiscountPercent() {
            return discountPercent;
        }

        public LocalDate getDueDate() {
            return dueDate;
        }

        public BigDecimal getDailyRentalCharge() {
            return dailyRentalCharge;
        }

        public long getDaysCharged() {
            return daysCharged;
        }

        public BigDecimal getPrediscountCharge() {
            return prediscountCharge;
        }

        public BigDecimal getDiscountAmount() {
            return discountAmount;
        }

        public BigDecimal getFinalCharge() {
            return finalCharge;
        }
    }

}