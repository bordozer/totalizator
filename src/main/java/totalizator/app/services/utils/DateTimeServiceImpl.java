package totalizator.app.services.utils;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

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
	public LocalDate plusDays( final LocalDate date, final int days ) {
		return date.plusDays( days );
	}

	@Override
	public String formatDateTimeUI( final LocalDateTime time ) {
		return formatDateTime( time, formatUI() );
	}

	private String formatDateTime( final LocalDateTime time, final String pattern ) {
		final DateTimeFormatter formatter = DateTimeFormatter.ofPattern( pattern, Locale.getDefault() );
		final LocalDateTime dateTime = localDateTime( time );

		return dateTime.format( formatter );
	}

	@Override
	public String formatDateTime( final LocalDateTime time ) {
		return formatDateTime( time, dateTimeFormat() );
	}

	@Override
	public LocalDateTime parseDateTime( final String time ) {
		return LocalDateTime.parse( time, DateTimeFormatter.ofPattern( dateTimeFormat() ) );
	}

	@Override
	public LocalDate parseDate( final String date ) {
		return LocalDate.parse( date, DateTimeFormatter.ofPattern( dateFormat() ) );
	}

	@Override
	public boolean hasTheSameDate( final LocalDateTime time1, final LocalDateTime time2 ) {
		final LocalDateTime from = LocalDateTime.of( time2.getYear(), time2.getMonth(), time2.getDayOfMonth(), 0, 0 ).minusSeconds( 1 );
		return time1.isAfter( from ) && time1.isBefore( LocalDateTime.from( from.plusDays( 1 ).minusSeconds( 1 ) ) );
	}

	@Override
	public boolean hasTheSameDate( final LocalDateTime time, final LocalDate date ) {
		return time.toLocalDate().isEqual( date );
	}

	private LocalDateTime localDateTime( final LocalDateTime time ) {
		return LocalDateTime.of( time.getYear(), time.getMonth(), time.getDayOfMonth(), time.getHour(), time.getMinute() );
	}

	private String dateFormat() {
		return DATE_FORMAT;
	}

	private String dateTimeFormat() {
		return String.format( "%s %s", DATE_FORMAT, TIME_FORMAT );
	}

	private String formatUI() {
		return DATE_TIME_FORMAT_UI;
	}
}
