package betmen.web.controllers.rest.admin.jobs.parameters;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RemoteGamesImportJobTaskParametersDTO extends AbstractJobTaskParametersDTO {
    private int cupId;
    private TimePeriodDTO timePeriod;
}
