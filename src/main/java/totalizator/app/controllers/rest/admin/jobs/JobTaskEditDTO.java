package totalizator.app.controllers.rest.admin.jobs;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import totalizator.app.controllers.rest.admin.jobs.parameters.AbstractJobTaskParametersDTO;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JobTaskEditDTO {

	private int jobTaskId;

	private String jobTaskName;
	private int jobTaskTypeId;
	private boolean jobTaskActive;

	private AbstractJobTaskParametersDTO jobTaskParametersHolder;

	private boolean jobTaskExecuting;

	public int getJobTaskId() {
		return jobTaskId;
	}

	public void setJobTaskId( final int jobTaskId ) {
		this.jobTaskId = jobTaskId;
	}

	public String getJobTaskName() {
		return jobTaskName;
	}

	public void setJobTaskName( final String jobTaskName ) {
		this.jobTaskName = jobTaskName;
	}

	public int getJobTaskTypeId() {
		return jobTaskTypeId;
	}

	public void setJobTaskTypeId( final int jobTaskTypeId ) {
		this.jobTaskTypeId = jobTaskTypeId;
	}

	public boolean isJobTaskActive() {
		return jobTaskActive;
	}

	public void setJobTaskActive( final boolean jobTaskActive ) {
		this.jobTaskActive = jobTaskActive;
	}

	public AbstractJobTaskParametersDTO getJobTaskParametersHolder() {
		return jobTaskParametersHolder;
	}

	public void setJobTaskParametersHolder( final AbstractJobTaskParametersDTO jobTaskParametersHolder ) {
		this.jobTaskParametersHolder = jobTaskParametersHolder;
	}

	public boolean isJobTaskExecuting() {
		return jobTaskExecuting;
	}

	public void setJobTaskExecuting( final boolean jobTaskExecuting ) {
		this.jobTaskExecuting = jobTaskExecuting;
	}

	@Override
	public String toString() {
		return String.format( "#%d: %s", jobTaskId, jobTaskName );
	}
}
