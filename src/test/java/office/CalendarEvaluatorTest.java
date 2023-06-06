package office;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Stream;

import static config.Common.dateFormatter;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CalendarEvaluatorTest {
    /*
    start date -> end date => expected response
     */
    private static Stream<Arguments> provideDates() {
        return Stream.of(
                // before the 4th, expect none
                Arguments.of(LocalDate.parse("07/01/20", dateFormatter), 2, 2),
                // after the 4th, but 4th was on weekend... don't count the 6th!!
                Arguments.of(LocalDate.parse("07/05/20", dateFormatter), 3, 2),
                // falls on a Saturday, returns the following Monday
                Arguments.of(LocalDate.parse("07/01/20", dateFormatter), 4, 2),
                // falls on a Sunday, returns the following Monday
                Arguments.of(LocalDate.parse("07/01/21", dateFormatter), 6, 3),
                // falls on a Monday, returns itself
                Arguments.of(LocalDate.parse("07/01/22", dateFormatter), 6, 3),
                // ends on a Monday, returns itself
                Arguments.of(LocalDate.parse("07/01/22", dateFormatter), 3, 0),
                // starts on a Monday, ignores it
                Arguments.of(LocalDate.parse("07/04/22", dateFormatter), 3, 3)
        );
    }

    @ParameterizedTest
    @MethodSource("provideDates")
    void test_calculateNumOfDaysToCharge(LocalDate startDate, int numOfRentalDays, long expected) {
        assertEquals(expected, CalendarEvaluator.calculateNumOfDaysToCharge(startDate, numOfRentalDays, false, false));
    }

    /*
    start date -> end date => expected response
     */
    private static Stream<Arguments> provideDatesForLaborDay() {
        return Stream.of(
                // before the Labor Day, expect none
                Arguments.of(LocalDate.parse("09/01/21", dateFormatter), LocalDate.parse("09/03/21", dateFormatter), Set.of()),
                // after the Labor Day, expect none
                Arguments.of(LocalDate.parse("09/07/21", dateFormatter), LocalDate.parse("09/21/21", dateFormatter), Set.of()),
                // wrong month, expect none
                Arguments.of(LocalDate.parse("07/07/21", dateFormatter), LocalDate.parse("07/21/21", dateFormatter), Set.of()),
                // happy path
                Arguments.of(LocalDate.parse("09/01/22", dateFormatter), LocalDate.parse("09/07/22", dateFormatter), Set.of(LocalDate.parse("09/05/22", dateFormatter))),
                // ends on Labor day, finds it
                Arguments.of(LocalDate.parse("09/01/22", dateFormatter), LocalDate.parse("09/05/22", dateFormatter), Set.of(LocalDate.parse("09/05/22", dateFormatter))),
                // starts on Labor day, ignores it
                Arguments.of(LocalDate.parse("09/05/22", dateFormatter), LocalDate.parse("09/07/22", dateFormatter), Set.of()),
                // spans more than a year, returns both
                Arguments.of(LocalDate.parse("09/01/20", dateFormatter), LocalDate.parse("09/07/21", dateFormatter), Set.of(LocalDate.parse("09/07/20", dateFormatter), LocalDate.parse("09/06/21", dateFormatter)))
        );
    }
}