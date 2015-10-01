package totalizator.app.services.jobs.jobDTO;

import com.google.gson.Gson;
import org.springframework.stereotype.Service;
import totalizator.app.controllers.rest.admin.jobs.logs.JobTaskDTO;
import totalizator.app.controllers.rest.admin.jobs.parameters.AbstractJobTaskParametersDTO;
import totalizator.app.controllers.rest.admin.jobs.parameters.RemoteGamesImportJobTaskParametersDTO;
import totalizator.app.controllers.rest.admin.jobs.parameters.TimePeriodDTO;
import totalizator.app.models.JobLog;
import totalizator.app.services.jobs.JobTask;
import totalizator.app.services.jobs.JobTaskType;
import totalizator.app.services.jobs.parameters.AbstractJobTaskParameters;
import totalizator.app.services.jobs.parameters.RemoteGamesImportJobTaskParameters;
import totalizator.app.services.jobs.results.GamesImportJobLogResultJSON;
import totalizator.app.services.utils.TimePeriod;
import totalizator.app.services.utils.TimePeriodType;

@Service
public class JobDTOServiceImpl implements JobDTOService {

	@Override
	public AbstractJobTaskParameters transformParametersFromDTO( final JobTaskType jobTaskType, final AbstractJobTaskParametersDTO parametersDTO ) {

		switch ( jobTaskType ) {
			case IMPORT_REMOTE_GAMES:
				final RemoteGamesImportJobTaskParametersDTO gamesImportParametersDTO = ( RemoteGamesImportJobTaskParametersDTO ) parametersDTO;

				final RemoteGamesImportJobTaskParameters result = new RemoteGamesImportJobTaskParameters();

				result.setCupId( gamesImportParametersDTO.getCupId() );
				result.setTimePeriod( getTimePeriod( gamesImportParametersDTO.getTimePeriod() ) );

				return result;
		}

		throw new IllegalArgumentException( String.format( "Unsupported job task type: %s", jobTaskType ) );
	}


	@Override
	public AbstractJobTaskParametersDTO transformParametersToDTO( final JobTaskType jobTaskType, final AbstractJobTaskParameters jobTaskParameters ) {

		switch ( jobTaskType ) {
			case IMPORT_REMOTE_GAMES:
				final RemoteGamesImportJobTaskParameters gamesImportJobTaskParameters = ( RemoteGamesImportJobTaskParameters ) jobTaskParameters;
				final RemoteGamesImportJobTaskParametersDTO result = new RemoteGamesImportJobTaskParametersDTO();

				result.setCupId( gamesImportJobTaskParameters.getCupId() );
				result.setTimePeriod( getTimePeriodDTO( gamesImportJobTaskParameters.getTimePeriod() ) );

				return result;
		}

		throw new IllegalArgumentException( String.format( "Unsupported job task type: %s", jobTaskType ) );
	}

	@Override
	public TimePeriod getTimePeriod( final TimePeriodDTO timePeriodDTO ) {

		final TimePeriod result = new TimePeriod();

		result.setTimePeriodType( TimePeriodType.getById( timePeriodDTO.getTimePeriodType() ) );
		result.setDateFrom( timePeriodDTO.getDateFrom() );
		result.setDateTo( timePeriodDTO.getDateTo() );
		result.setDaysOffset( timePeriodDTO.getDaysOffset() );
		result.setMonthsOffset( timePeriodDTO.getMonthsOffset() );

		return result;
	}

	@Override
	public TimePeriodDTO getTimePeriodDTO( final TimePeriod timePeriod ) {

		final TimePeriodDTO timePeriodDTO = new TimePeriodDTO();

		timePeriodDTO.setTimePeriodType( timePeriod.getTimePeriodType().getId() );
		timePeriodDTO.setDateFrom( timePeriod.getDateFrom() );
		timePeriodDTO.setDateTo( timePeriod.getDateTo() );
		timePeriodDTO.setDaysOffset( timePeriod.getDaysOffset() );
		timePeriodDTO.setMonthsOffset( timePeriod.getMonthsOffset() );

		return timePeriodDTO;
	}

	@Override
	public JobTaskDTO transformJobTask( final JobTask jobTask ) {

		final JobTaskDTO jobTaskDTO = new JobTaskDTO();

		jobTaskDTO.setJobTaskId( jobTask.getId() );
		jobTaskDTO.setJobTaskName( jobTask.getJobName() );

		return jobTaskDTO;
	}

	@Override
	public GamesImportJobLogResultJSON transformFromJobGamesImportSpecificResultJSON( final JobLog jobLog ) {
		return new Gson().fromJson( jobLog.getJobResultJSON(), GamesImportJobLogResultJSON.class );
	}
}
