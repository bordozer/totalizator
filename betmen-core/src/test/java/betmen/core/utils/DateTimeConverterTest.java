package betmen.core.utils;

import org.junit.Test;

import java.util.Date;
import java.util.TimeZone;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class DateTimeConverterTest {

    private static final TimeZone UTC_TIME_ZONE = TimeZone.getTimeZone("UTC");
    private static final TimeZone GMT_MINUS_12_TIME_ZONE = TimeZone.getTimeZone("GMT-12:00");
    private static final TimeZone GMT_MINUS_4_TIME_ZONE = TimeZone.getTimeZone("GMT-4:00");
    private static final TimeZone GMT_PLUS_1_TIME_ZONE = TimeZone.getTimeZone("GMT+1:00");
    private static final TimeZone GMT_PLUS_3_TIME_ZONE = TimeZone.getTimeZone("GMT+3:00");
    private static final TimeZone GMT_PLUS_14_TIME_ZONE = TimeZone.getTimeZone("GMT+14:00");

    private static final int DATE_1970_01_02_AT_11_00 = 35 * 60 * 60 * 1000; // 1970-01-02 11:00

    @Test
    public void shouldFormatDateTime0() {
        assertThat(DateTimeConverter.formatDateTime(new Date(DATE_1970_01_02_AT_11_00)), is("02/01/1970 11:00"));
    }

    @Test
    public void shouldFormatDateTime1() {
        assertThat(DateTimeConverter.formatDateTime(new Date(DATE_1970_01_02_AT_11_00), UTC_TIME_ZONE), is("02/01/1970 11:00"));
    }

    @Test
    public void shouldFormatDateTime2() {
        assertThat(DateTimeConverter.formatDateTime(new Date(DATE_1970_01_02_AT_11_00), GMT_MINUS_12_TIME_ZONE), is("01/01/1970 23:00"));
    }

    @Test
    public void shouldFormatDateTime3() {
        assertThat(DateTimeConverter.formatDateTime(new Date(DATE_1970_01_02_AT_11_00), GMT_MINUS_4_TIME_ZONE), is("02/01/1970 07:00"));
    }

    @Test
    public void shouldFormatDateTime4() {
        assertThat(DateTimeConverter.formatDateTime(new Date(DATE_1970_01_02_AT_11_00), GMT_PLUS_1_TIME_ZONE), is("02/01/1970 12:00"));
    }

    @Test
    public void shouldFormatDateTime5() {
        assertThat(DateTimeConverter.formatDateTime(new Date(DATE_1970_01_02_AT_11_00), GMT_PLUS_3_TIME_ZONE), is("02/01/1970 14:00"));
    }

    @Test
    public void shouldFormatDateTime6() {
        assertThat(DateTimeConverter.formatDateTime(new Date(DATE_1970_01_02_AT_11_00), GMT_PLUS_14_TIME_ZONE), is("03/01/1970 01:00"));
    }

}