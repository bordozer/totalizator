package betmen.web.controllers.rest.admin.jobs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class JobTaskIdDTO {
    private final int jobTaskId;
    private final int jobTaskTypeId;
}
