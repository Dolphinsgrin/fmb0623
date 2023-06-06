package office;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.Map;

import static java.time.DayOfWeek.MONDAY;
import static java.time.DayOfWeek.SATURDAY;
import static java.time.DayOfWeek.SUNDAY;

public class CalendarEvaluator {
    /**
     * Returns the number of days to charge rental fees based on the checkout date, the number of days the rental is
     * requested, and whether to include weekends and/or holidays.
     * @param checkoutDate the date the tool was rented-out
     * @param numOfRentalDays the total number of days the rental is required
     * @param isWeekendCharged whether rental fees are charged for weekend days
     * @param isHolidayCharged whether rental fees are charged for holidays
     * @return the number of days to charge the daily rate for the tool rental
     */
    public static long calculateNumOfDaysToCharge(LocalDate checkoutDate, int numOfRentalDays, boolean isWeekendCharged, boolean isHolidayCharged) {
        LocalDate endDate = checkoutDate.plusDays(numOfRentalDays);
        Map<LocalDate, Boolean> datesFlaggedForCharge = new HashMap<>();
        /* offsetting the date window by 1 to make start date exclusive and final date inclusive
        keeping the offsetting constrained to this class to avoid off-by-one issues elsewhere in code
         */
        checkoutDate.plusDays(1).datesUntil(endDate.plusDays(1)).forEach(d -> datesFlaggedForCharge.put(d, true));
        if (!isWeekendCharged) {
            maskWeekends(datesFlaggedForCharge);
        }
        if (!isHolidayCharged) {
            maskHolidays(datesFlaggedForCharge);
        }
        return datesFlaggedForCharge.entrySet().stream().filter(Map.Entry::getValue).count();
    }

    private static void maskWeekends(final Map<LocalDate, Boolean> dateSet) {
        dateSet.entrySet().stream()
                .filter(es -> es.getKey().getDayOfWeek() == SATURDAY || es.getKey().getDayOfWeek() == SUNDAY)
                .forEach(es -> dateSet.computeIfPresent(es.getKey(), (k, v) -> false));
    }

    public static void maskHolidays(final Map<LocalDate, Boolean> dateSet) {
        dateSet.forEach((key, value) -> {
            // mask July 4ths
            if (key.getMonth() == Month.JULY && key.getDayOfMonth() == 4) {
                if (key.getDayOfWeek() == SATURDAY) {
                    dateSet.computeIfPresent(key.plusDays(2), (k, v) -> false);
                } else if (key.getDayOfWeek() == SUNDAY) {
                    dateSet.computeIfPresent(key.plusDays(1), (k, v) -> false);
                } else {
                    dateSet.computeIfPresent(key, (k, v) -> false);
                }
            // mask Labor Day
            } else if (key.getMonth() == Month.SEPTEMBER && key.getDayOfWeek() == MONDAY && key.getDayOfMonth() <= 7) {
                dateSet.computeIfPresent(key, (k, v) -> false);
            }
        });
    }
}
