package totalizator.app.controllers.rest.admin.jobs.logs;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import totalizator.app.controllers.rest.admin.jobs.parameters.AbstractJobTaskParametersDTO;
import totalizator.app.dto.serialization.DateTimeDeserializer;
import totalizator.app.dto.serialization.DateTimeSerializer;
import totalizator.app.services.jobs.results.JobLogResultJSON;

import java.time.LocalDateTime;

public class JobLogDTO {

	private int jobTaskLogId;

	private int jobTaskId;
	private LocalDateTime startTime;
	private LocalDateTime finishTime;
	private LocalDateTime jobTaskInternalTime;
	private int jobExecutionStateId;

	private AbstractJobTaskParametersDTO jobTaskParameters;
	private int jobTaskTypeId;

	private JobLogResultJSON jobLogResultJSON;

	public int getJobTaskLogId() {
		return jobTaskLogId;
	}

	public void setJobTaskLogId( final int jobTaskLogId ) {
		this.jobTaskLogId = jobTaskLogId;
	}

	public int getJobTaskId() {
		return jobTaskId;
	}

	public void setJobTaskId( final int jobTaskId ) {
		this.jobTaskId = jobTaskId;
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
	public LocalDateTime getFinishTime() {
		return finishTime;
	}

	@JsonDeserialize( using = DateTimeDeserializer.class )
	public void setFinishTime( final LocalDateTime finishTime ) {
		this.finishTime = finishTime;
	}

	@JsonSerialize( using = DateTimeSerializer.class )
	public LocalDateTime getJobTaskInternalTime() {
		return jobTaskInternalTime;
	}

	@JsonDeserialize( using = DateTimeDeserializer.class )
	public void setJobTaskInternalTime( final LocalDateTime jobTaskInternalTime ) {
		this.jobTaskInternalTime = jobTaskInternalTime;
	}

	public int getJobExecutionStateId() {
		return jobExecutionStateId;
	}

	public void setJobExecutionStateId( final int jobExecutionStateId ) {
		this.jobExecutionStateId = jobExecutionStateId;
	}

	public AbstractJobTaskParametersDTO getJobTaskParameters() {
		return jobTaskParameters;
	}

	public void setJobTaskParameters( final AbstractJobTaskParametersDTO jobTaskParameters ) {
		this.jobTaskParameters = jobTaskParameters;
	}

	public void setJobTaskTypeId( final int jobTaskTypeId ) {
		this.jobTaskTypeId = jobTaskTypeId;
	}

	public int getJobTaskTypeId() {
		return jobTaskTypeId;
	}

	public void setJobLogResultJSON( final JobLogResultJSON jobLogResultJSON ) {
		this.jobLogResultJSON = jobLogResultJSON;
	}

	public JobLogResultJSON getJobLogResultJSON() {
		return jobLogResultJSON;
	}
}
