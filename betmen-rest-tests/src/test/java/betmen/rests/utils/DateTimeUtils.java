package betmen.rests.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DateTimeUtils {

    private final static String DATE_FORMAT = "dd/MM/yyyy";
    private final static String TIME_FORMAT = "HH:mm";

    public static LocalDate parseDate(final String date) {
        return LocalDate.parse(date, getDateFormatter());
    }

    public static LocalDateTime parseDateTime(final String time) {
        return LocalDateTime.parse(time, getDateTimeFormatter());
    }

    public static String formatDate(final LocalDate date) {
        return date.format(getDateFormatter());
    }

    public static String formatDate(final LocalDateTime dateTime) {
        return dateTime.toLocalDate().format(getDateFormatter());
    }

    public static String formatDateTime(final LocalDate date) {
        return date.format(getDateTimeFormatter());
    }

    private static DateTimeFormatter getDateFormatter() {
        return getFormatter(DATE_FORMAT);
    }

    private static DateTimeFormatter getDateTimeFormatter() {
        return getFormatter(String.format("%s %s", DATE_FORMAT, TIME_FORMAT));
    }

    private static DateTimeFormatter getFormatter(final String format) {
        return DateTimeFormatter.ofPattern(format, Locale.getDefault());
    }
}
