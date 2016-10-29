package betmen.rests.common;

import betmen.dto.utils.Parameters;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DtoDateTimeSerializer implements JsonSerializer<LocalDateTime> {

    @Override
    public JsonElement serialize(final LocalDateTime src, final Type typeOfSrc, final JsonSerializationContext context) {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(String.format("%s %s", Parameters.DATE_FORMAT, Parameters.TIME_FORMAT));
        String ret = src.format(formatter);
        return new JsonPrimitive(ret);
    }
}
