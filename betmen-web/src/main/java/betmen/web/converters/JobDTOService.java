package betmen.web.converters;

import betmen.web.controllers.rest.admin.jobs.logs.JobTaskDTO;
import betmen.web.controllers.rest.admin.jobs.parameters.AbstractJobTaskParametersDTO;
import betmen.web.controllers.rest.admin.jobs.parameters.TimePeriodDTO;
import betmen.core.entity.JobLog;
import betmen.core.service.jobs.JobTask;
import betmen.core.service.jobs.JobTaskType;
import betmen.core.service.jobs.parameters.AbstractJobTaskParameters;
import betmen.core.service.jobs.results.GamesImportJobLogResultJSON;
import betmen.core.service.utils.TimePeriod;

public interface JobDTOService {

    AbstractJobTaskParameters transformParametersFromDTO(final JobTaskType jobTaskType, final AbstractJobTaskParametersDTO parametersDTO);

    TimePeriod transformToTimePeriod(final TimePeriodDTO timePeriodDTO);

    AbstractJobTaskParametersDTO transformParametersToDTO(final JobTaskType jobTaskType, final AbstractJobTaskParameters jobTaskParameters);

    TimePeriodDTO transformToTimePeriodDTO(final TimePeriod timePeriod);

    JobTaskDTO transformJobTask(final JobTask jobTask);

    GamesImportJobLogResultJSON transformFromJobGamesImportSpecificResultJSON(final JobLog jobLog);
}
