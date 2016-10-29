package betmen.rests.common;

import betmen.dto.utils.Parameters;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DtoDateSerializer implements JsonSerializer<LocalDate> {

    @Override
    public JsonElement serialize(final LocalDate src, final Type typeOfSrc, final JsonSerializationContext context) {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Parameters.DATE_FORMAT);
        String ret = src.format(formatter);
        return new JsonPrimitive(ret);
    }
}
