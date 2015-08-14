package totalizator.app.dto.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import totalizator.app.services.utils.DateTimeService;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateDeserializer extends JsonDeserializer<LocalDate> {

	@Override
	public LocalDate deserialize( final JsonParser jp, final DeserializationContext ctxt ) throws IOException {
		final DateTimeFormatter formatter = DateTimeFormatter.ofPattern( DateTimeService.DATE_FORMAT );
		return LocalDate.parse( jp.getText(), formatter );
	}
}
