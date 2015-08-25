package totalizator.app.services.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface DateTimeService {

	String DATE_FORMAT = "dd/MM/yyyy";
	String TIME_FORMAT = "HH:mm";
	String DATE_TIME_FORMAT_UI = "dd MMM yyyy HH:mm";

	LocalDateTime getNow();

	LocalDateTime minusHours( final int hours );

	LocalDateTime plusHours( int hours );

	LocalDateTime minusDays( final int days );


	LocalDateTime minusHours( final LocalDateTime time, int hours );

	LocalDateTime plusHours( final LocalDateTime time, int hours );

	LocalDateTime minusDays( final LocalDateTime time, final int days );

	LocalDateTime plusDays( final LocalDateTime time, final int days );

	LocalDate plusDays( final LocalDate date, final int days );


	String formatDateTimeUI( final LocalDateTime time );

	String formatDateTime( final LocalDateTime time );


	LocalDateTime parseDateTime( final String time );

	LocalDate parseDate( final String date );

	boolean hasTheSameDate( final LocalDateTime time1, final LocalDateTime time2 );

	boolean hasTheSameDate( final LocalDateTime time, final LocalDate date );

	String dateTimeFormat();
}
