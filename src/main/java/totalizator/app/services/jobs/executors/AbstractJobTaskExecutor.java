package totalizator.app.services.jobs.executors;

import totalizator.app.services.jobs.JobTask;
import totalizator.app.services.jobs.results.JobLogResultJSON;
import totalizator.app.services.utils.DateTimeService;

import java.time.LocalDateTime;

public abstract class AbstractJobTaskExecutor implements Runnable {

	protected final JobTask jobTask;
	protected final LocalDateTime jobTaskInternalTime;

	private JobTaskExecutionMonitor executionMonitor;

	private JobExecutionState jobExecutionState = JobExecutionState.IDLE;

	protected DateTimeService dateTimeService;

	protected JobTaskExecutionCallback executionCallback;

	protected JobLogResultJSON jobLogResultJSON;

	private int jobLogId;

	protected abstract void execute();

	protected AbstractJobTaskExecutor( final JobTask jobTask, final LocalDateTime jobTaskInternalTime ) {
		this.jobTask = jobTask;
		this.jobTaskInternalTime = jobTaskInternalTime;
	}

	@Override
	public void run() {

		executionStateInProgress();

		execute();

		afterJobDone();
	}

	public final JobTaskExecutionMonitor getExecutionMonitor() {
		return executionMonitor;
	}

	public final void setExecutionMonitor( final JobTaskExecutionMonitor executionMonitor ) {
		this.executionMonitor = executionMonitor;
	}

	public final JobTask getJobTask() {
		return jobTask;
	}

	public final LocalDateTime getJobTaskInternalTime() {
		return jobTaskInternalTime;
	}

	public final boolean isRunning() {
		return JobExecutionState.RUNNING_STATES.contains( jobExecutionState );
	}

	public final boolean isStoppedByAnyReason() {
		return JobExecutionState.STOPPED_STATES.contains( jobExecutionState );
	}

	public JobExecutionState getJobExecutionState() {
		return jobExecutionState;
	}

	public final void executionStateInProgress() {
		this.jobExecutionState = JobExecutionState.IN_PROGRESS;
	}

	public final void executionStateStoppedByUser() {
		finishJobExecution( JobExecutionState.STOPPED_BY_USER );
	}

	public JobTaskExecutionCallback getExecutionCallback() {
		return executionCallback;
	}

	public void setExecutionCallback( final JobTaskExecutionCallback executionCallback ) {
		this.executionCallback = executionCallback;
	}

	protected void afterJobDone() {
		executionCallback.afterJobDone();
	};

	protected final void executionStateFinished() {
		finishJobExecution( JobExecutionState.FINISHED );
	}

	protected final void executionStateError() {
		finishJobExecution( JobExecutionState.ERROR );
	}

	private void finishJobExecution( final JobExecutionState reason ) {

		this.jobExecutionState = reason;

		executionMonitor.setFinishingTime( dateTimeService.getNow() );
	}

	protected final void setTotal( final int total ){
		executionMonitor.setTotalSteps( total );
	}

	protected final void increase(){
		executionMonitor.increase();
	}

	public void setDateTimeService( final DateTimeService dateTimeService ) {
		this.dateTimeService = dateTimeService;
	}

	public void setJobLogId( final int jobLogId ) {
		this.jobLogId = jobLogId;
	}

	public int getJobLogId() {
		return jobLogId;
	}

	public JobLogResultJSON getJobLogResultJSON() {
		return jobLogResultJSON;
	}

	public void setJobLogResultJSON( final JobLogResultJSON jobLogResultJSON ) {
		this.jobLogResultJSON = jobLogResultJSON;
	}

	@Override
	public int hashCode() {
		return 31 * jobTask.getId();
	}

	@Override
	public boolean equals( final Object obj ) {

		if ( obj == null ) {
			return false;
		}

		if ( obj == this ) {
			return true;
		}

		if ( ! obj.getClass().equals( AbstractJobTaskExecutor.class ) ) {
			return false;
		}

		final AbstractJobTaskExecutor executor = ( AbstractJobTaskExecutor ) obj;
		return executor.getJobTask().equals( jobTask );
	}
}
