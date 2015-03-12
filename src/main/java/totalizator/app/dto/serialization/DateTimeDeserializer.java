package totalizator.app.dto.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeDeserializer extends JsonDeserializer<Date> {

	public static final String DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm";

	private static final Logger LOGGER = Logger.getLogger( DateTimeDeserializer.class );

	@Override
	public Date deserialize( final JsonParser jp, final DeserializationContext ctxt ) throws IOException, JsonProcessingException {
		final SimpleDateFormat formatter = new SimpleDateFormat( DATE_TIME_FORMAT );
		try {
			final Date date = formatter.parse( "1970/01/01 " + jp.getText() );
			return new Date( date.getTime() );
		} catch ( ParseException e ) {
			LOGGER.error( String.format( "Can not deserialize date: '%s'", jp.getText() ) );
		}

		return null;
	}
}
