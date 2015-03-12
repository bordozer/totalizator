package totalizator.app.dto.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeSerializer extends JsonSerializer<Date> {

	@Override
	public void serialize( final Date value, final JsonGenerator jgen, final SerializerProvider provider ) throws IOException, JsonProcessingException {
		final SimpleDateFormat formatter = new SimpleDateFormat( DateTimeDeserializer.DATE_TIME_FORMAT );
		jgen.writeString( formatter.format( value ) );
	}
}
