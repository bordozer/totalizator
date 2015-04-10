package totalizator.app.services.utils;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

@Service
public class DateTimeServiceImpl implements DateTimeService {

	private static final String FORMAT_DATE = "yyyy-MM-dd";
	private static final String FORMAT_TIME = "HH:mm";

	@Override
	public LocalDateTime getNow() {
		return LocalDateTime.now();
	}

	@Override
	public LocalDateTime offset( final int measure, final int offset ) {
		return offset( getNow(), measure, offset );
	}

	@Override
	public LocalDateTime offset( final LocalDateTime time, final int measure, final int offset ) {

//		final Calendar cal = getCalendar( time );
//		cal.add( measure, offset );

		return time; // TODO
	}

	@Override
	public String formatDateTime( final LocalDateTime time ) {

		final DateTimeFormatter formatter = DateTimeFormatter.ofPattern( format() );
		final LocalDateTime dateTime = localDateTime( time );

		return dateTime.format( formatter );
	}

	@Override
	public LocalDateTime parseDate( final String date ) {

		final DateTimeFormatter formatter = DateTimeFormatter.ofPattern( format() );

		return LocalDateTime.parse( date, formatter );
	}

	private LocalDateTime localDateTime( final LocalDateTime time ) {
		return LocalDateTime.of( time.getYear(), time.getMonth(), time.getDayOfMonth(), time.getHour(), time.getMinute() );
	}

	private Calendar getCalendar( final Date date ) {

		final Calendar calendar = getCalendar();
		calendar.setTime( date );

		return calendar;
	}

	private Calendar getCalendar() {
		final Calendar calendar = Calendar.getInstance();
		calendar.setFirstDayOfWeek( Calendar.MONDAY );

		return calendar;
	}

	private String format() {
		return String.format( "%s %s", FORMAT_DATE, FORMAT_TIME );
	}
}
