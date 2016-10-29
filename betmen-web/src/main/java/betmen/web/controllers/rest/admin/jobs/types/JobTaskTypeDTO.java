package betmen.web.controllers.rest.admin.jobs.types;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class JobTaskTypeDTO {
    private final int jobTaskTypeId;
    private final String jobTaskTypeName;
}
