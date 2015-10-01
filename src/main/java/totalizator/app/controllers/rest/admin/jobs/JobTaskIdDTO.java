package totalizator.app.controllers.rest.admin.jobs;

public class JobTaskIdDTO {

	private final int jobTaskId;
	private final int jobTaskTypeId;

	public JobTaskIdDTO( final int jobTaskId, final int jobTaskTypeId ) {
		this.jobTaskId = jobTaskId;
		this.jobTaskTypeId = jobTaskTypeId;
	}

	public int getJobTaskId() {
		return jobTaskId;
	}

	public int getJobTaskTypeId() {
		return jobTaskTypeId;
	}
}
