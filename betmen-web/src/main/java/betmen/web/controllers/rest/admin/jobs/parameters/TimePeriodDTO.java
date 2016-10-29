package betmen.web.controllers.rest.admin.jobs.parameters;

import betmen.dto.serialization.DateDeserializer;
import betmen.dto.serialization.DateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class TimePeriodDTO {
    private int timePeriodType;

    @JsonSerialize(using = DateSerializer.class)
    @JsonDeserialize(using = DateDeserializer.class)
    private LocalDate dateFrom;

    @JsonSerialize(using = DateSerializer.class)
    @JsonDeserialize(using = DateDeserializer.class)
    private LocalDate dateTo;

    private int daysOffset;
    private int monthsOffset;
}
