package totalizator.app.services.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface DateTimeService {

	String DATE_FORMAT = "dd/MM/yyyy";
	String TIME_FORMAT = "HH:mm";
	String DATE_FORMAT_UI = "dd MMM yyyy";
	String TIME_FORMAT_UI = "HH:mm";

	LocalDateTime getNow();

	LocalDate getToday();

	LocalDateTime minusHours( final int hours );

	LocalDateTime plusHours( int hours );

	LocalDateTime minusDays( final int days );


	LocalDateTime minusHours( final LocalDateTime time, int hours );

	LocalDateTime plusHours( final LocalDateTime time, int hours );

	LocalDateTime minusDays( final LocalDateTime time, final int days );

	LocalDateTime plusDays( final LocalDateTime time, final int days );

	LocalDate plusDays( final LocalDate date, final int days );

	LocalDateTime plusDays( final int days );

	LocalDateTime plusMonths( final LocalDateTime time, final int months );


	String formatTime( final LocalDateTime time );

	String formatDateTimeUI( final LocalDateTime time );

	String formatDateUI( final LocalDateTime time );

	String formatDateTime( final LocalDateTime time );


	LocalDateTime parseDateTime( final String time );

	LocalDate parseDate( final String date );

	boolean hasTheSameDate( final LocalDateTime time1, final LocalDateTime time2 );

	boolean hasTheSameDate( final LocalDateTime time, final LocalDate date );

	String dateDateFormat();

	String dateTimeFormat();

	int diffInDays( final LocalDate date1, final LocalDate date2 );

	LocalDateTime getFirstSecondOf( final LocalDate date );

	LocalDateTime getLastSecondOf( final LocalDate date );

	LocalDate firstDayOfMonth( final LocalDateTime time );

	LocalDate lastDayOfMonth( final LocalDateTime time );
}
