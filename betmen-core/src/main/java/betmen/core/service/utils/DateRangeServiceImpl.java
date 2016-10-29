package betmen.core.service.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class DateRangeServiceImpl implements DateRangeService {

    @Autowired
    private DateTimeService dateTimeService;


    @Override
    public LocalDate getDateFrom(final TimePeriod timePeriod, final LocalDateTime time) {

        if (timePeriod.getTimePeriodType() == TimePeriodType.DAYS_OFFSET) {
            return dateTimeService.getToday();
        }

        if (timePeriod.getTimePeriodType() == TimePeriodType.MONTH_OFFSET) {
            return dateTimeService.firstDayOfMonth(dateTimeService.plusMonths(time, timePeriod.getMonthsOffset()));
        }

        return timePeriod.getDateFrom();
    }

    @Override
    public LocalDate getDateTo(final TimePeriod timePeriod, final LocalDateTime time) {

        if (timePeriod.getTimePeriodType() == TimePeriodType.DAYS_OFFSET) {
            return dateTimeService.plusDays(time, timePeriod.getDaysOffset()).toLocalDate();
        }

        if (timePeriod.getTimePeriodType() == TimePeriodType.MONTH_OFFSET) {
            return dateTimeService.lastDayOfMonth(dateTimeService.plusMonths(time, timePeriod.getMonthsOffset()));
        }

        return timePeriod.getDateTo();
    }
}
