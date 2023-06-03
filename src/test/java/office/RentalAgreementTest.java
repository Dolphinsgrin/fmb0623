package office;

import config.ConfigReader;
import org.junit.jupiter.api.Test;
import shed.Shed;
import shed.Tool;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static office.RentalAgreement.BAD_DISCOUNT_PERCENTAGE_MESSAGE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RentalAgreementTest {
    static final Shed testShed = ConfigReader.readConfig(Shed.FILE_NAME, Shed.class);
    static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yy");

    @Test
    void test_challenge_proof_test1() {
        int badDiscount = 101;
        String expectedErrorMessage = String.format(BAD_DISCOUNT_PERCENTAGE_MESSAGE, badDiscount);
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> new RentalAgreement(testShed.getTool("JAKR"), 5, LocalDate.parse("09/03/15", dateFormatter), badDiscount));
        assertEquals(e.getMessage(), expectedErrorMessage);
    }

    @Test
    void test_challenge_proof_test2() {
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
                1.99,
                3, // July 4th falls on a Saturday, => not observed until Monday the 6th.
                5.97,
                .60,
                5.37
        );
        // generate the rental agreement
        RentalAgreement underTest = new RentalAgreement(testShed.getTool(code), rentalDays, LocalDate.parse(checkoutDayString, dateFormatter), discountPercentage);
        underTest.printAgreement();
        // test the results
        validateScenario(underTest, expected);
    }

    @Test
    void test_challenge_proof_test3() {
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
                1.49,
                3, // charge the weekend, but not the Monday
                4.47,
                1.12,
                3.35
        );
        // generate the rental agreement
        RentalAgreement underTest = new RentalAgreement(testShed.getTool(code), rentalDays, LocalDate.parse(checkoutDayString, dateFormatter), discountPercentage);
        underTest.printAgreement();
        // test the results
        validateScenario(underTest, expected);
    }

    @Test
    void test_challenge_proof_test4() {
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
                2.99,
                3, // no charge over weekend, no charge on Monday (Labor day)
                8.97,
                0,
                8.97
        );
        // generate the rental agreement
        RentalAgreement underTest = new RentalAgreement(testShed.getTool(code), rentalDays, LocalDate.parse(checkoutDayString, dateFormatter), discountPercentage);
        underTest.printAgreement();
        // test the results
        validateScenario(underTest, expected);
    }

    @Test
    void test_challenge_proof_test5() {
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
                2.99,
                5, // no charge over weekend, no charge on Monday (Labor day)
                14.95,
                0,
                14.95
        );
        // generate the rental agreement
        RentalAgreement underTest = new RentalAgreement(testShed.getTool(code), rentalDays, LocalDate.parse(checkoutDayString, dateFormatter), discountPercentage);
        underTest.printAgreement();
        // test the results
        validateScenario(underTest, expected);
    }

    @Test
    void test_challenge_proof_test6() {
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
                2.99,
                1, // no charge over weekend, no charge on Monday (deferred 4th of July)
                2.99,
                1.50, // 1.495 rounding up!
                1.49
        );
        // generate the rental agreement
        RentalAgreement underTest = new RentalAgreement(testShed.getTool(code), rentalDays, LocalDate.parse(checkoutDayString, dateFormatter), discountPercentage);
        underTest.printAgreement();
        // test the results
        validateScenario(underTest, expected);
    }

    void validateScenario(RentalAgreement input, StaticValues expected) {
        assertEquals(expected.getTool().getCode(), input.getTool().getCode(), "failed code");
        assertEquals(expected.getTool().getType(), input.getTool().getType(), "failed type");
        assertEquals(expected.getTool().getBrand(), input.getTool().getBrand(), "failed brand");
        assertEquals(expected.getRentalDays(), input.getRentalDays(), "failed rental days");
        assertEquals(expected.getCheckoutDate(), input.getCheckoutDate(), "failed checkout date");
        assertEquals(expected.getDiscountPercent(), input.getDiscountPercent(), "failed discount percent");
        assertEquals(expected.getDueDate(), input.getDueDate(), "failed due date");
        assertEquals(expected.getDailyRentalCharge(), input.getDailyRentalCharge(), "failed daily rental charge");
        assertEquals(expected.getDaysCharged(), input.getDaysCharged(), "failed days charged");
        assertEquals(expected.getPrediscountCharge(), input.getPrediscountCharge(), "failed pre-discount charge");
        assertEquals(expected.getDiscountAmount(), input.getDiscountAmount(), "failed discount amount");
        assertEquals(expected.getFinalCharge(), input.getFinalCharge(), "failed final charge");
    }

    private static class StaticValues {
        private final Tool tool;
        private final int rentalDays;
        private final LocalDate checkoutDate;
        private final int discountPercent;
        private final LocalDate dueDate;
        private final double dailyRentalCharge;
        private final long daysCharged;
        private final double prediscountCharge;
        private final double discountAmount;
        private final double finalCharge;

        public StaticValues(Tool tool, int rentalDays, LocalDate checkoutDate, int discountPercent, LocalDate dueDate, double dailyRentalCharge, long daysCharged, double prediscountCharge, double discountAmount, double finalCharge) {
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

        public double getDailyRentalCharge() {
            return dailyRentalCharge;
        }

        public long getDaysCharged() {
            return daysCharged;
        }

        public double getPrediscountCharge() {
            return prediscountCharge;
        }

        public double getDiscountAmount() {
            return discountAmount;
        }

        public double getFinalCharge() {
            return finalCharge;
        }
    }

}