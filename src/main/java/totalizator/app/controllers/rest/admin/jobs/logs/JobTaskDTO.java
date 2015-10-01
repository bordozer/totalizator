package totalizator.app.controllers.rest.admin.jobs.logs;

public class JobTaskDTO {

	private int jobTaskId;
	private String jobTaskName;

	public void setJobTaskId( final int jobTaskId ) {
		this.jobTaskId = jobTaskId;
	}

	public int getJobTaskId() {
		return jobTaskId;
	}

	public void setJobTaskName( final String jobTaskName ) {
		this.jobTaskName = jobTaskName;
	}

	public String getJobTaskName() {
		return jobTaskName;
	}
}
