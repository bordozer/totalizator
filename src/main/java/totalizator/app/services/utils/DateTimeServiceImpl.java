package totalizator.app.services.utils;

import org.springframework.stereotype.Service;

import java.time.Duration;
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
	public LocalDate getToday() {
		return LocalDate.now();
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
	public LocalDateTime plusDays( final int days ) {
		return plusDays( getNow(), days );
	}

	@Override
	public String formatTime( final LocalDateTime time ) {
		return time.format( DateTimeFormatter.ofPattern( getTimeFormat(), Locale.getDefault() ) );
	}

	@Override
	public String formatDateTimeUI( final LocalDateTime time ) {
		return formatDateTime( time, getFormatDateTimeUI() );
	}

	@Override
	public String formatDateUI( final LocalDateTime time ) {
		return formatDate( time.toLocalDate(), getFormatDateUI() );
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
		return LocalDate.parse( date, DateTimeFormatter.ofPattern( getDateFormat() ) );
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

	@Override
	public int diffInDays( final LocalDate date1, final LocalDate date2 ) {
		return ( int ) Duration.between( date1.atStartOfDay(), date2.atStartOfDay() ).toDays();
	}

	@Override
	public LocalDateTime getFirstSecondOf( final LocalDate date ) {
		return LocalDateTime.of( date.getYear(), date.getMonth(), date.getDayOfMonth(), 0, 0 );
	}

	@Override
	public LocalDateTime getLastSecondOf( final LocalDate date ) {
		return getFirstSecondOf( date.plusDays( 1 ).atStartOfDay().toLocalDate() );
	}

	@Override
	public String dateDateFormat() {
		return String.format( "%s", DATE_FORMAT );
	}

	@Override
	public String dateTimeFormat() {
		return String.format( "%s %s", DATE_FORMAT, TIME_FORMAT );
	}

	private LocalDateTime localDateTime( final LocalDateTime time ) {
		return LocalDateTime.of( time.getYear(), time.getMonth(), time.getDayOfMonth(), time.getHour(), time.getMinute() );
	}

	private String formatDate( final LocalDate date, final String format ) {
		return date.format( DateTimeFormatter.ofPattern( format, Locale.getDefault() ) );
	}

	private String getDateFormat() {
		return DATE_FORMAT;
	}

	private String getTimeFormat() {
		return TIME_FORMAT;
	}

	private String getFormatDateTimeUI() {
		return String.format( "%s %s", DATE_FORMAT_UI, TIME_FORMAT_UI );
	}

	private String getFormatDateUI() {
		return DATE_FORMAT_UI;
	}
}
