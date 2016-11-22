package betmen.web.converters;

import betmen.core.entity.JobLog;
import betmen.core.service.jobs.JobTask;
import betmen.core.service.jobs.JobTaskType;
import betmen.core.service.jobs.parameters.AbstractJobTaskParameters;
import betmen.core.service.jobs.parameters.RemoteGamesImportJobTaskParameters;
import betmen.core.service.jobs.results.GamesImportJobLogResultJSON;
import betmen.core.service.utils.TimePeriod;
import betmen.core.service.utils.TimePeriodType;
import betmen.web.controllers.rest.admin.jobs.logs.JobTaskDTO;
import betmen.web.controllers.rest.admin.jobs.parameters.AbstractJobTaskParametersDTO;
import betmen.web.controllers.rest.admin.jobs.parameters.RemoteGamesImportJobTaskParametersDTO;
import betmen.web.controllers.rest.admin.jobs.parameters.TimePeriodDTO;
import com.google.gson.Gson;
import org.springframework.stereotype.Service;

@Service
public class JobDTOServiceImpl implements JobDTOService {

    @Override
    public AbstractJobTaskParameters transformParametersFromDTO(final JobTaskType jobTaskType, final AbstractJobTaskParametersDTO parametersDTO) {

        switch (jobTaskType) {
            case IMPORT_REMOTE_GAMES:
                final RemoteGamesImportJobTaskParametersDTO gamesImportParametersDTO = (RemoteGamesImportJobTaskParametersDTO) parametersDTO;

                final RemoteGamesImportJobTaskParameters result = new RemoteGamesImportJobTaskParameters();

                result.setCupId(gamesImportParametersDTO.getCupId());
                result.setTimePeriod(transformToTimePeriod(gamesImportParametersDTO.getTimePeriod()));

                return result;
        }

        throw new IllegalArgumentException(String.format("Unsupported job task type: %s", jobTaskType));
    }


    @Override
    public AbstractJobTaskParametersDTO transformParametersToDTO(final JobTaskType jobTaskType, final AbstractJobTaskParameters jobTaskParameters) {

        switch (jobTaskType) {
            case IMPORT_REMOTE_GAMES:
                final RemoteGamesImportJobTaskParameters gamesImportJobTaskParameters = (RemoteGamesImportJobTaskParameters) jobTaskParameters;
                final RemoteGamesImportJobTaskParametersDTO result = new RemoteGamesImportJobTaskParametersDTO();

                result.setCupId(gamesImportJobTaskParameters.getCupId());
                result.setTimePeriod(transformToTimePeriodDTO(gamesImportJobTaskParameters.getTimePeriod()));

                return result;
        }

        throw new IllegalArgumentException(String.format("Unsupported job task type: %s", jobTaskType));
    }

    @Override
    public TimePeriod transformToTimePeriod(final TimePeriodDTO timePeriodDTO) {

        final TimePeriod result = new TimePeriod();

        result.setTimePeriodType(TimePeriodType.getById(timePeriodDTO.getTimePeriodType()));
        result.setDateFrom(timePeriodDTO.getDateFrom());
        result.setDateTo(timePeriodDTO.getDateTo());
        result.setDaysOffset(timePeriodDTO.getDaysOffset());
        result.setMonthsOffset(timePeriodDTO.getMonthsOffset());

        return result;
    }

    @Override
    public TimePeriodDTO transformToTimePeriodDTO(final TimePeriod timePeriod) {

        final TimePeriodDTO timePeriodDTO = new TimePeriodDTO();

        timePeriodDTO.setTimePeriodType(timePeriod.getTimePeriodType().getId());
        timePeriodDTO.setDateFrom(timePeriod.getDateFrom());
        timePeriodDTO.setDateTo(timePeriod.getDateTo());
        timePeriodDTO.setDaysOffset(timePeriod.getDaysOffset());
        timePeriodDTO.setMonthsOffset(timePeriod.getMonthsOffset());

        return timePeriodDTO;
    }

    @Override
    public JobTaskDTO transformJobTask(final JobTask jobTask) {

        final JobTaskDTO jobTaskDTO = new JobTaskDTO();

        jobTaskDTO.setJobTaskId(jobTask.getId());
        jobTaskDTO.setJobTaskName(jobTask.getJobName());

        return jobTaskDTO;
    }

    @Override
    public GamesImportJobLogResultJSON transformFromJobGamesImportSpecificResultJSON(final JobLog jobLog) {
        return new Gson().fromJson(jobLog.getJobResultJSON(), GamesImportJobLogResultJSON.class);
    }
}
