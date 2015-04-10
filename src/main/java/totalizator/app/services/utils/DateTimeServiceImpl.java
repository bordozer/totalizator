package totalizator.app.services.utils;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class DateTimeServiceImpl implements DateTimeService {

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

	private String format() {
		return FORMAT_DATE_TIME;
	}
}
