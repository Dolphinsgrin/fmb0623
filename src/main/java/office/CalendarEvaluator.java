package office;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.time.DayOfWeek.MONDAY;
import static java.time.DayOfWeek.SATURDAY;
import static java.time.DayOfWeek.SUNDAY;

public class CalendarEvaluator {
    public static List<LocalDate> daysToCharge(LocalDate startDate, int numOfDays, boolean chargeWeekends, boolean chargeHolidays) {
        // offsetting the date window by 1 to make start date exclusive and final date inclusive
        final LocalDate effectiveStartDate = startDate.plusDays(1);
        LocalDate endDate = effectiveStartDate.plusDays(numOfDays);
        return effectiveStartDate.datesUntil(endDate)
                // apply a weekend mask (if we're not charging for the weekends)
                .filter(d -> {
                    if (chargeWeekends) {
                        return true;
                    } else {
                        Set<DayOfWeek> weekDaySet = Set.of(SATURDAY, SUNDAY);
                        return !weekDaySet.contains(d.getDayOfWeek());
                    }
                })
                // apply a holiday mask (if we're not charging for holidays)
                .filter(d -> {
                    if (chargeHolidays) {
                        return true;
                    } else {
                        return !getHolidaySet(effectiveStartDate, endDate).contains(d);
                    }
                })
                .toList();
    }

    public static Set<LocalDate> getHolidaySet(LocalDate startDate, LocalDate endDate) {
        Set<LocalDate> holidaySet = findEffectiveJuly4ths(startDate.plusDays(1), endDate);
        holidaySet.addAll(findLaborDays(startDate, endDate));
        return holidaySet;
    }

    public static Set<LocalDate> findEffectiveJuly4ths(LocalDate startDate, LocalDate endDate) {
        return startDate.datesUntil(endDate)
                .filter(d -> d.getMonth() == Month.JULY && d.getDayOfMonth() == 4)
                // offsetting weekend occurrences of July 4th to the following Monday
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
