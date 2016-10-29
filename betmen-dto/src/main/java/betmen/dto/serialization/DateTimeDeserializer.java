package betmen.dto.serialization;

import betmen.dto.utils.Parameters;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

    private static final Logger LOGGER = Logger.getLogger(DateTimeDeserializer.class);

    @Override
    public LocalDateTime deserialize(final JsonParser jp, final DeserializationContext ctxt) throws IOException {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(String.format("%s %s", Parameters.DATE_FORMAT, Parameters.TIME_FORMAT));
        return LocalDateTime.parse(jp.getText(), formatter);
    }
}
