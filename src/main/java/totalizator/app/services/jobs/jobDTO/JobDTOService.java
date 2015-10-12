package totalizator.app.services.jobs.jobDTO;

import totalizator.app.controllers.rest.admin.jobs.logs.JobTaskDTO;
import totalizator.app.controllers.rest.admin.jobs.parameters.AbstractJobTaskParametersDTO;
import totalizator.app.controllers.rest.admin.jobs.parameters.TimePeriodDTO;
import totalizator.app.models.JobLog;
import totalizator.app.services.jobs.JobTask;
import totalizator.app.services.jobs.JobTaskType;
import totalizator.app.services.jobs.parameters.AbstractJobTaskParameters;
import totalizator.app.services.jobs.results.GamesImportJobLogResultJSON;
import totalizator.app.services.utils.TimePeriod;

public interface JobDTOService {

	AbstractJobTaskParameters transformParametersFromDTO( final JobTaskType jobTaskType, final AbstractJobTaskParametersDTO parametersDTO );

	TimePeriod transformToTimePeriod( final TimePeriodDTO timePeriodDTO );

	AbstractJobTaskParametersDTO transformParametersToDTO( final JobTaskType jobTaskType, final AbstractJobTaskParameters jobTaskParameters );

	TimePeriodDTO transformToTimePeriodDTO( final TimePeriod timePeriod );

	JobTaskDTO transformJobTask( final JobTask jobTask );

	GamesImportJobLogResultJSON transformFromJobGamesImportSpecificResultJSON( final JobLog jobLog );
}
