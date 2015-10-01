package totalizator.app.controllers.rest.admin.jobs.execute;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import totalizator.app.dto.serialization.DateTimeDeserializer;
import totalizator.app.dto.serialization.DateTimeSerializer;

import java.time.LocalDateTime;

public class JobTaskExecutionMonitorDTO {

	private int totalSteps;
	private int currentStep;

	private LocalDateTime startTime;
	private int jobExecutionStateId;
	private boolean jobExecuting;
	private LocalDateTime finishingTime;

	public int getTotalSteps() {
		return totalSteps;
	}

	public void setTotalSteps( final int totalSteps ) {
		this.totalSteps = totalSteps;
	}

	public int getCurrentStep() {
		return currentStep;
	}

	public void setCurrentStep( final int currentStep ) {
		this.currentStep = currentStep;
	}

	@JsonSerialize( using = DateTimeSerializer.class )
	public LocalDateTime getStartTime() {
		return startTime;
	}

	@JsonDeserialize( using = DateTimeDeserializer.class )
	public void setStartTime( final LocalDateTime startTime ) {
		this.startTime = startTime;
	}

	@JsonSerialize( using = DateTimeSerializer.class )
	public LocalDateTime getFinishingTime() {
		return finishingTime;
	}

	@JsonDeserialize( using = DateTimeDeserializer.class )
	public void setFinishingTime( final LocalDateTime finishingTime ) {
		this.finishingTime = finishingTime;
	}

	public int getJobExecutionStateId() {
		return jobExecutionStateId;
	}

	public void setJobExecutionStateId( final int jobExecutionStateId ) {
		this.jobExecutionStateId = jobExecutionStateId;
	}

	public void setJobExecuting( final boolean jobExecuting ) {
		this.jobExecuting = jobExecuting;
	}

	public boolean isJobExecuting() {
		return jobExecuting;
	}
}
