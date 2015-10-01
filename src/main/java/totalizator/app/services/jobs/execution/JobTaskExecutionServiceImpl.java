package totalizator.app.services.jobs.execution;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import totalizator.app.models.JobLog;
import totalizator.app.services.CupService;
import totalizator.app.services.jobs.JobLogService;
import totalizator.app.services.jobs.JobTask;
import totalizator.app.services.jobs.JobTaskService;
import totalizator.app.services.jobs.executors.*;
import totalizator.app.services.jobs.results.GamesImportJobLogResultJSON;
import totalizator.app.services.jobs.results.JobLogResultJSON;
import totalizator.app.services.matches.imports.RemoteGameDataImportService;
import totalizator.app.services.utils.DateRangeService;
import totalizator.app.services.utils.DateTimeService;
import totalizator.app.services.utils.JsonService;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class JobTaskExecutionServiceImpl implements JobTaskExecutionService {

	@Autowired
	private JobTaskService jobTaskService;

	@Autowired
	private CupService cupService;

	@Autowired
	private RemoteGameDataImportService remoteGameDataImportService;

	@Autowired
	private DateRangeService dateRangeService;

	@Autowired
	private DateTimeService dateTimeService;

	@Autowired
	private JobLogService jobLogService;

	@Autowired
	private JsonService jsonService;

	private final ConcurrentHashMap<Integer, AbstractJobTaskExecutor> activeJobs = new ConcurrentHashMap<Integer, AbstractJobTaskExecutor>();

	@Override
	public void execute( final int jobTaskId ) {
		execute( jobTaskId, dateTimeService.getNow() );
	}

	@Override
	public void execute( final int jobTaskId, final LocalDateTime jobTaskInternalTime ) {

		if ( isJobExecutingNow( jobTaskId ) ) {
			return;
		}

		final JobTask jobTask = jobTaskService.load( jobTaskId );

		final LocalDateTime startTime = dateTimeService.getNow();
		final int jobLogId = createJobLog( jobTask, jobTaskInternalTime, startTime );

		final AbstractJobTaskExecutor jobTaskExecutor = getInstance( jobTask, jobTaskInternalTime, startTime, jobLogId );

		activeJobs.put( jobTaskId, jobTaskExecutor );

		jobTaskExecutor.start();
	}

	@Override
	public void stop( final int jobTaskId ) {

		final AbstractJobTaskExecutor jobExecutor = getJobExecutor( jobTaskId );

		if ( jobExecutor == null ) {
			return;
		}

		jobExecutor.executionStateStoppedByUser();

		removeJobFromActive( jobTaskId );
	}

	@Override
	public AbstractJobTaskExecutor getJobExecutor( final int jobTaskId ) {
		return activeJobs.get( jobTaskId );
	}

	@Override
	public boolean isJobExecutingNow( final int jobTaskId ) {
		return getJobExecutor( jobTaskId ) != null;
	}

	private int createJobLog( final JobTask jobTask, final LocalDateTime jobTaskInternalTime, final LocalDateTime startTime ) {

		final JobLog jobLog = new JobLog();

		jobLog.setJobTaskId( jobTask.getId() );
		jobLog.setJobTaskType( jobTask.getJobTaskType() );
		jobLog.setStartTime( startTime );
		jobLog.setJobTaskInternalTime( jobTaskInternalTime );
		jobLog.setJobParametersJSON( jsonService.toJson( jobTask.getJobTaskParameters() ) );
		jobLog.setJobExecutionState( JobExecutionState.PREPARING_FOR_RUNNING );

		final JobLog saved = jobLogService.save( jobLog );

		return saved.getId();
	}

	private AbstractJobTaskExecutor getInstance( final JobTask jobTask, final LocalDateTime jobTaskInternalTime, LocalDateTime startTime, final int jobLogId ) {

		final GamesImportJobTaskExecutor taskExecutor;

		switch ( jobTask.getJobTaskType() ) {
			case IMPORT_REMOTE_GAMES:
				taskExecutor = new GamesImportJobTaskExecutor( jobTask, jobTaskInternalTime );
				taskExecutor.setJobLogResultJSON( new GamesImportJobLogResultJSON() );
				break;
			default:
				throw new IllegalArgumentException( String.format( "Unsupported job task type: %s", jobTask.getJobTaskType() ) );
		}

		taskExecutor.setCupService( cupService );
		taskExecutor.setRemoteGameDataImportService( remoteGameDataImportService );
		taskExecutor.setDateRangeService( dateRangeService );
		taskExecutor.setExecutionMonitor( new JobTaskExecutionMonitor( startTime ) );
		taskExecutor.setDateTimeService( dateTimeService );
		taskExecutor.setExecutionCallback( callback( taskExecutor ) );
		taskExecutor.setJobLogId( jobLogId );

		return taskExecutor;
	}

	private void removeJobFromActive( final int jobTaskId ) {
		activeJobs.remove( jobTaskId );
	}

	private JobTaskExecutionCallback callback( final GamesImportJobTaskExecutor taskExecutor ) {

		return new JobTaskExecutionCallback( taskExecutor ) {

			@Override
			public void afterJobDone() {

				removeJobFromActive( jobTaskExecutor.getJobTask().getId() );

				final JobTaskExecutionMonitor monitor = jobTaskExecutor.getExecutionMonitor();

				final JobLog jobLog = jobLogService.load( jobTaskExecutor.getJobLogId() );

				jobLog.setFinishTime( monitor.getFinishingTime() );
				jobLog.setJobExecutionState( jobTaskExecutor.getJobExecutionState() );

				jobLog.setJobResultJSON( getJobLogResultJSON( monitor ) );

				jobLogService.save( jobLog );
			}

			private String getJobLogResultJSON( final JobTaskExecutionMonitor monitor ) {

				final JobLogResultJSON jobLogResultJSON = jobTaskExecutor.getJobLogResultJSON();
				jobLogResultJSON.setTotalSteps( monitor.getTotalSteps() );
				jobLogResultJSON.setPerformedSteps( monitor.getCurrentStep() );

				return jsonService.toJson( jobLogResultJSON );
			}
		};
	}
}
