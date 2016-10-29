package betmen.core.service.utils;

import java.time.LocalDate;

public class TimePeriod {

    private TimePeriodType timePeriodType;

    private LocalDate dateFrom;
    private LocalDate dateTo;

    private int daysOffset;
    private int monthsOffset;

    public TimePeriodType getTimePeriodType() {
        return timePeriodType;
    }

    public void setTimePeriodType(final TimePeriodType timePeriodType) {
        this.timePeriodType = timePeriodType;
    }

    public LocalDate getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(final LocalDate dateFrom) {
        this.dateFrom = dateFrom;
    }

    public LocalDate getDateTo() {
        return dateTo;
    }

    public void setDateTo(final LocalDate dateTo) {
        this.dateTo = dateTo;
    }

    public int getDaysOffset() {
        return daysOffset;
    }

    public void setDaysOffset(final int daysOffset) {
        this.daysOffset = daysOffset;
    }

    public int getMonthsOffset() {
        return monthsOffset;
    }

    public void setMonthsOffset(final int monthsOffset) {
        this.monthsOffset = monthsOffset;
    }
}
