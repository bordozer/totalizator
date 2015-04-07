package totalizator.app.services.utils;

import org.springframework.stereotype.Service;
import totalizator.app.services.utils.DateTimeService;

import java.util.Calendar;
import java.util.Date;

@Service
public class DateTimeServiceImpl implements DateTimeService {

	@Override
	public Date getNow() {
		return new Date();
	}

	@Override
	public Date offset( final int measure, final int offset ) {
		return offset( new Date(), measure, offset );
	}

	@Override
	public Date offset( final Date time, final int measure, final int offset ) {
		final Calendar cal = getCalendar( time );
		cal.add( measure, offset );

		return cal.getTime();
	}

	@Override
	public String formatDate( final Date date ) {
		return date.toString(); //TODO;
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
}
