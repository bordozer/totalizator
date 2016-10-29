package betmen.core.utils;

import betmen.core.service.utils.DateTimeService;
import org.springframework.util.Assert;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Date;
import java.util.TimeZone;

public class DateTimeConverter {

    public static final TimeZone UTC_TIME_ZONE = TimeZone.getTimeZone("UTC");
    public static final ZoneId UTC_ZONE_ID = ZoneId.of("UTC");

    public static final String DATE_FORMAT = DateTimeService.DATE_FORMAT;
    public static final String TIME_FORMAT = DateTimeService.TIME_FORMAT;
    public static final String DATE_TIME_FORMAT = String.format("%s %s", DATE_FORMAT, TIME_FORMAT);

    // TODO: test
    public static Date parseDateTime(final String dateTime, final TimeZone tz) {
        Assert.notNull(dateTime, "Date must not be null");
        Assert.notNull(tz, "Timezone must not be null");
        return doParseDateTime(dateTime, tz);
    }

    // TODO: test
    public static Date parseDateTime(final String date, final String time, final TimeZone tz) {
        Assert.notNull(date, "Date must not be null");
        Assert.notNull(time, "Time must not be null");
        Assert.notNull(tz, "Timezone must not be null");
        return doParseDateTime(String.format("%s %s", date, time), tz);
    }

    /**
     * @param dateTime - UTC Date
     * @return         - parsed date and time
     */
    public static String formatDateTime(final Date dateTime) {
        return formatDateTime(dateTime, UTC_TIME_ZONE);
    }

    /**
     * @param dateTime - UTC Date
     * @param tz       - time zone
     * @return         - parsed date and time
     */
    public static String formatDateTime(final Date dateTime, final TimeZone tz) {
        Assert.notNull(dateTime, "Date must not be null");
        Assert.notNull(tz, "Timezone must not be null");
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_TIME_FORMAT);
        formatter.setTimeZone(tz);
        return formatter.format(dateTime.getTime());
    }

    private static Date doParseDateTime(final String dateTime, final TimeZone tz) {
        final SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT);
        sdf.setTimeZone(tz);
        try {
            return sdf.parse(dateTime);
        } catch (ParseException e) {
            // TODO: log error
            return null;
        }
    }
}
