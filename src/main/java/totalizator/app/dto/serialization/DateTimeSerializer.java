package totalizator.app.dto.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import totalizator.app.services.utils.DateTimeService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeSerializer extends JsonSerializer<LocalDateTime> {

	@Override
	public void serialize( final LocalDateTime value, final JsonGenerator jgen, final SerializerProvider provider ) throws IOException, JsonProcessingException {
		final DateTimeFormatter formatter = DateTimeFormatter.ofPattern( DateTimeService.DATE_TIME_FORMAT );
		jgen.writeString( value.format( formatter ) );
	}
}
