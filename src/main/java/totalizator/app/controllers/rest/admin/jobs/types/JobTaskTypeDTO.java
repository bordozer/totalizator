package totalizator.app.controllers.rest.admin.jobs.types;

public class JobTaskTypeDTO {

	private final int jobTaskTypeId;
	private final String jobTaskTypeName;

	public JobTaskTypeDTO( final int jobTaskTypeId, final String jobTaskTypeName ) {
		this.jobTaskTypeId = jobTaskTypeId;
		this.jobTaskTypeName = jobTaskTypeName;
	}

	public int getJobTaskTypeId() {
		return jobTaskTypeId;
	}

	public String getJobTaskTypeName() {
		return jobTaskTypeName;
	}

	@Override
	public String toString() {
		return String.format( "%d: %s", jobTaskTypeId, jobTaskTypeName );
	}
}
