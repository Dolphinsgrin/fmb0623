package office;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.Set;
import java.util.stream.Collectors;

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
    public static int calculateNumOfDaysToCharge(LocalDate checkoutDate, int numOfRentalDays, boolean isWeekendCharged, boolean isHolidayCharged) {
        /* offsetting the date window by 1 to make start date exclusive and final date inclusive
        keeping the offsetting constrained to this class to avoid off-by-one issues elsewhere in code
         */
        final LocalDate effectiveStartDate = checkoutDate.plusDays(1);
        LocalDate endDate = effectiveStartDate.plusDays(numOfRentalDays);
        return effectiveStartDate.datesUntil(endDate)
                // apply a weekend mask (if we're not charging for the weekends)
                .filter(d -> {
                    if (isWeekendCharged) {
                        return true;
                    } else {
                        Set<DayOfWeek> weekDaySet = Set.of(SATURDAY, SUNDAY);
                        return !weekDaySet.contains(d.getDayOfWeek());
                    }
                })
                // apply a holiday mask (if we're not charging for holidays)
                .filter(d -> {
                    if (isHolidayCharged) {
                        return true;
                    } else {
                        return !getHolidaySet(effectiveStartDate, endDate).contains(d);
                    }
                })
                .toList().size();
    }

    private static Set<LocalDate> getHolidaySet(LocalDate startDate, LocalDate endDate) {
        Set<LocalDate> holidaySet = findEffectiveJuly4ths(startDate.plusDays(1), endDate);
        holidaySet.addAll(findLaborDays(startDate, endDate));
        return holidaySet;
    }

    public static Set<LocalDate> findEffectiveJuly4ths(LocalDate startDate, LocalDate endDate) {
        return startDate.datesUntil(endDate)
                .filter(d -> d.getMonth() == Month.JULY && d.getDayOfMonth() == 4)
                /* Offsetting weekend occurrences of July 4th to the following Monday. This has the potential of pushing
                the holiday past the dueDate, but as we're not checking past the due date it will ultimately be ignored */
                .map(d -> {
                    if (d.getDayOfWeek() == SATURDAY) {
                        return d.plusDays(2);
                    }
                    if (d.getDayOfWeek() == SUNDAY) {
                        return d.plusDays(1);
                    }
                    return d;
                })
                .collect(Collectors.toSet());
    }

    public static Set<LocalDate> findLaborDays(LocalDate startDate, LocalDate endDate) {
        // identify the 1st Monday of September
        return startDate.datesUntil(endDate)
                .filter(d -> d.getMonth() == Month.SEPTEMBER)
                .filter(d -> d.getDayOfWeek() == MONDAY)
                .filter(d -> d.getDayOfMonth() <= 7)
                .collect(Collectors.toSet());
    }
}
