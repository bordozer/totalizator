package betmen.web.controllers.rest.admin.jobs.parameters;

import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RemoteGamesImportJobTaskParametersDTO extends AbstractJobTaskParametersDTO {
    private int cupId;
    private TimePeriodDTO timePeriod;
}
