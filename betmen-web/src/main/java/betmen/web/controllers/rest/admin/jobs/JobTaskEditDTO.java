package betmen.web.controllers.rest.admin.jobs;

import betmen.web.controllers.rest.admin.jobs.parameters.AbstractJobTaskParametersDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class JobTaskEditDTO {
    private int jobTaskId;
    private String jobTaskName;
    private int jobTaskTypeId;
    private boolean jobTaskActive;
    private AbstractJobTaskParametersDTO jobTaskParametersHolder;
    private boolean jobTaskExecuting;
}
