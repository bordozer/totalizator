package betmen.web.controllers.rest.admin.jobs;

import betmen.web.controllers.rest.admin.jobs.parameters.RemoteGamesImportJobTaskParametersDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobTaskGamesImportEditDTO extends JobTaskEditDTO {
    private RemoteGamesImportJobTaskParametersDTO jobTaskParametersHolder;
}
