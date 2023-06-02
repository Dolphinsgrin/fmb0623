package office;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import shed.Shed;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

class RentalAgreementTest {
    static Shed testShed;
    @BeforeAll
    static void setUp() {
        testShed = Shed.InitShed();
    }
    @Test
    void test_RentalAgreement() {
        RentalAgreement underTest = new RentalAgreement(testShed.getTool("LADW"), 5, LocalDate.now(), 10);
        underTest.printAgreement();
    }

    @Test
    void test_challenge_proof() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/uu");
        List<RentalAgreement> testScenarios = Arrays.asList(
                new RentalAgreement(testShed.getTool("JAKR"), 5, LocalDate.parse("09/03/15", dateFormatter), 101),
                new RentalAgreement(testShed.getTool("LADW"), 3, LocalDate.parse("07/02/20", dateFormatter), 10),
                new RentalAgreement(testShed.getTool("CHNS"), 5, LocalDate.parse("07/02/15", dateFormatter), 25),
                new RentalAgreement(testShed.getTool("JAKD"), 6, LocalDate.parse("09/03/15", dateFormatter), 0),
                new RentalAgreement(testShed.getTool("JAKR"), 9, LocalDate.parse("07/02/15", dateFormatter), 0),
                new RentalAgreement(testShed.getTool("JAKR"), 4, LocalDate.parse("07/02/20", dateFormatter), 50)
        );
        // TODO: determine assertions
        for (RentalAgreement ra : testScenarios) {
            ra.printAgreement();
            System.out.println();
        }
    }

}