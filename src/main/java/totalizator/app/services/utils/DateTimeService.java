package totalizator.app.services.utils;

import java.time.LocalDateTime;

public interface DateTimeService {

	String DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm";
	String DATE_TIME_FORMAT_UI = "dd MMM yyyy HH:mm";

	LocalDateTime getNow();

	LocalDateTime minusHours( final int hours );

	LocalDateTime plusHours( int hours );

	LocalDateTime minusDays( final int days );


	LocalDateTime minusHours( final LocalDateTime time, int hours );

	LocalDateTime plusHours( final LocalDateTime time, int hours );

	LocalDateTime minusDays( final LocalDateTime time, final int days );

	LocalDateTime plusDays( final LocalDateTime time, final int days );


	String formatDateTimeUI( final LocalDateTime time );

	LocalDateTime parseDate( final String date );

	boolean hasTheSameDate( final LocalDateTime time1, final LocalDateTime time2 );
}
