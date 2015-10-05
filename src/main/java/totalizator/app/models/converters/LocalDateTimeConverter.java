package totalizator.app.models.converters;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.TimeZone;

@Converter
public class LocalDateTimeConverter implements AttributeConverter<LocalDateTime, Date> {

	@Override
	public Date convertToDatabaseColumn( final LocalDateTime localDateTime ) {

		if ( localDateTime == null ) {
			return null;
		}

		final TimeZone timeZone = TimeZone.getDefault();
		final int currentOffset = timeZone.getOffset( System.currentTimeMillis() );

		return Date.from( localDateTime.toInstant( ZoneOffset.ofTotalSeconds( currentOffset / 1000 ) ) );
	}

	@Override
	public LocalDateTime convertToEntityAttribute( final Date value ) {

		if ( value == null ) {
			return null;
		}

		return LocalDateTime.ofInstant( value.toInstant(), ZoneId.systemDefault() );
	}
}
