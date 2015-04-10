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
	public LocalDateTime minusHours( final int hours ) {
		return minusHours( getNow(), hours );
	}

	@Override
	public LocalDateTime minusDays( final int days ) {
		return minusDays( getNow(), days );
	}

	@Override
	public LocalDateTime plusHours( final int hours ) {
		return plusHours( getNow(), hours );
	}

	@Override
	public LocalDateTime plusHours( final LocalDateTime time, final int hours ) {
		return time.plusHours( hours );
	}

	@Override
	public LocalDateTime minusHours( final LocalDateTime time, final int hours ) {
		return time.minusHours( hours );
	}

	@Override
	public LocalDateTime minusDays( final LocalDateTime time, final int days ) {
		return time.minusDays( days );
	}

	@Override
	public LocalDateTime plusDays( final LocalDateTime time, final int days ) {
		return time.plusDays( days );
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
		return DATE_TIME_FORMAT;
	}
}
