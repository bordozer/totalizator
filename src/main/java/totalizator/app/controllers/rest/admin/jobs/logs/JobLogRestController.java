package totalizator.app.controllers.rest.admin.jobs.logs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import totalizator.app.models.JobLog;
import totalizator.app.services.jobs.JobLogService;
import totalizator.app.services.jobs.JobTaskService;
import totalizator.app.services.jobs.JobTaskType;
import totalizator.app.services.jobs.jobDTO.JobDTOService;
import totalizator.app.services.jobs.results.JobLogResultJSON;
import totalizator.app.services.utils.JsonService;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping( "/admin/rest/jobs" )
public class JobLogRestController {

	@Autowired
	private JobLogService jobLogService;

	@Autowired
	private JobTaskService jobTaskService;

	@Autowired
	private JsonService jsonService;

	@Autowired
	private JobDTOService jobDTOService;

	@RequestMapping( method = RequestMethod.GET, value = "/logs/" )
	public JobLogsDTO getJobLogs() {
		return transformJobLogs( jobLogService.loadAll() );
	}

	@RequestMapping( method = RequestMethod.GET, value = "/{jobTaskId}/logs/" )
	public JobLogsDTO getJobLog( final @PathVariable int jobTaskId ) {

		final JobLogsDTO jobLogsDTO = transformJobLogs( jobLogService.loadAll( jobTaskId ) );
		jobLogsDTO.setJobTask( jobDTOService.transformJobTask( jobTaskService.load( jobTaskId ) ) );

		return jobLogsDTO;
	}

	@RequestMapping( method = RequestMethod.POST, value = "/logs/delete/" )
	public void deleteLogEntries( final @RequestParam( "jobTaskLogIds[]" ) String[] jobTaskLogIds ) {
		for ( final String jobTaskLogId : jobTaskLogIds ) {
			jobLogService.delete( Integer.valueOf( jobTaskLogId ) );
		}
	}

	private JobLogsDTO transformJobLogs( final List<JobLog> jobLogs ) {

		return new JobLogsDTO( jobLogs
				.stream()
				.map( toDTOMapper() )
				.collect( Collectors.toList() ) );
	}

	private Function<JobLog, JobLogDTO> toDTOMapper() {

		return new Function<JobLog, JobLogDTO>() {

			@Override
			public JobLogDTO apply( final JobLog jobLog ) {

				final JobLogDTO jobLogDTO = new JobLogDTO();

				jobLogDTO.setJobTaskLogId( jobLog.getId() );

				jobLogDTO.setJobTaskId( jobLog.getJobTaskId() );
				jobLogDTO.setStartTime( jobLog.getStartTime() );
				jobLogDTO.setFinishTime( jobLog.getFinishTime() );
				jobLogDTO.setJobTaskInternalTime( jobLog.getJobTaskInternalTime() );
				jobLogDTO.setJobExecutionStateId( jobLog.getJobExecutionState().getId() );
				jobLogDTO.setJobTaskTypeId( jobLog.getJobTaskType().getId() );
				jobLogDTO.setJobTaskParameters( jobDTOService.transformParametersToDTO( jobLog.getJobTaskType(), jsonService.fromJson( jobLog.getJobParametersJSON() ) ) );

				jobLogDTO.setJobLogResultJSON( getJobLogResultJSON( jobLog ) );

				return jobLogDTO;
			}
		};
	}

	private JobLogResultJSON getJobLogResultJSON( final JobLog jobLog ) {

		if ( jobLog.getJobTaskType() == JobTaskType.IMPORT_REMOTE_GAMES ) {
			return jobDTOService.transformFromJobGamesImportSpecificResultJSON( jobLog );
		}

		throw new IllegalArgumentException( String.format( "Unsupported JobTaskType: %s", jobLog.getJobTaskType() ) );
	}

}
