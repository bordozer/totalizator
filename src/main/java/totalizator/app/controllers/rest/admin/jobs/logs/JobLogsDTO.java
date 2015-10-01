package totalizator.app.controllers.rest.admin.jobs.logs;

import java.util.List;

public class JobLogsDTO {

	private final List<JobLogDTO> jobTaskLogs;
	private JobTaskDTO jobTask;

	public JobLogsDTO( final List<JobLogDTO> jobTaskLogs ) {
		this.jobTaskLogs = jobTaskLogs;
	}

	public JobTaskDTO getJobTask() {
		return jobTask;
	}

	public void setJobTask( final JobTaskDTO jobTask ) {
		this.jobTask = jobTask;
	}

	public List<JobLogDTO> getJobTaskLogs() {
		return jobTaskLogs;
	}
}
