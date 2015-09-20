package totalizator.app.models;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;

@Converter
public class LocalDateTimeConverter implements AttributeConverter<LocalDateTime, Date> {

	@Override
	public Date convertToDatabaseColumn( final LocalDateTime localDateTime ) {

		if ( localDateTime == null ) {
			return null;
		}

		return Date.from( localDateTime.toInstant( ZoneOffset.UTC ) );
	}

	@Override
	public LocalDateTime convertToEntityAttribute( final Date value ) {

		if ( value == null ) {
			return null;
		}

		return LocalDateTime.ofInstant( value.toInstant(), ZoneId.systemDefault() );
	}
}
