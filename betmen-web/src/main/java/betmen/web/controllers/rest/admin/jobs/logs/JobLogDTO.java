package betmen.web.controllers.rest.admin.jobs.logs;

import betmen.core.service.jobs.results.JobLogResultJSON;
import betmen.dto.serialization.DateTimeDeserializer;
import betmen.dto.serialization.DateTimeSerializer;
import betmen.web.controllers.rest.admin.jobs.parameters.AbstractJobTaskParametersDTO;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class JobLogDTO {
    private int jobTaskLogId;
    private int jobTaskId;

    @JsonSerialize(using = DateTimeSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer.class)
    private LocalDateTime startTime;

    @JsonSerialize(using = DateTimeSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer.class)
    private LocalDateTime finishTime;

    private LocalDateTime jobTaskInternalTime;
    private int jobExecutionStateId;
    private AbstractJobTaskParametersDTO jobTaskParameters;
    private int jobTaskTypeId;
    private JobLogResultJSON jobLogResultJSON;
}
