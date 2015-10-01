package totalizator.app.models;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import totalizator.app.models.converters.JobExecutionStateConverter;
import totalizator.app.models.converters.JobTaskTypeConverter;
import totalizator.app.models.converters.LocalDateTimeConverter;
import totalizator.app.services.jobs.JobTaskType;
import totalizator.app.services.jobs.executors.JobExecutionState;

import javax.persistence.*;
import java.time.LocalDateTime;

import static totalizator.app.models.JobLog.LOAD_ALL;
import static totalizator.app.models.JobLog.LOAD_ALL_FRO_JOB_TASK;

@Entity
@org.hibernate.annotations.Cache( region = "common", usage = CacheConcurrencyStrategy.READ_WRITE )
@Table( name = "jobLog" )
@NamedQueries( {
		@NamedQuery(
				name = LOAD_ALL,
				query = "select c from JobLog c order by startTime desc"
		),
		@NamedQuery(
				name = LOAD_ALL_FRO_JOB_TASK,
				query = "select c from JobLog c where jobTaskId= :jobTaskId order by startTime desc"
		)
} )
public class JobLog extends AbstractEntity {

	public static final String LOAD_ALL = "jobLog.loadAll";
	public static final java.lang.String LOAD_ALL_FRO_JOB_TASK = "jobLog.loadAllForJobTask";

	private int jobTaskId;

	@Convert( converter = LocalDateTimeConverter.class )
	private LocalDateTime startTime;

	@Convert( converter = LocalDateTimeConverter.class )
	private LocalDateTime finishTime;

	@Convert( converter = LocalDateTimeConverter.class )
	private LocalDateTime jobTaskInternalTime;

	@Column( columnDefinition = "VARCHAR(255)" )
	private String jobParametersJSON;

	@Column( columnDefinition = "VARCHAR(255)" )
	private String jobResultJSON;

	@Convert( converter = JobExecutionStateConverter.class )
	@Column( columnDefinition = "INT(1)" )
	private JobExecutionState jobExecutionState;

	@Column( columnDefinition = "INT(2)" )
	@Convert( converter = JobTaskTypeConverter.class )
	private JobTaskType jobTaskType;

	public int getJobTaskId() {
		return jobTaskId;
	}

	public void setJobTaskId( final int jobTaskId ) {
		this.jobTaskId = jobTaskId;
	}

	public LocalDateTime getStartTime() {
		return startTime;
	}

	public void setStartTime( final LocalDateTime startTime ) {
		this.startTime = startTime;
	}

	public LocalDateTime getFinishTime() {
		return finishTime;
	}

	public void setFinishTime( final LocalDateTime finishTime ) {
		this.finishTime = finishTime;
	}

	public LocalDateTime getJobTaskInternalTime() {
		return jobTaskInternalTime;
	}

	public void setJobTaskInternalTime( final LocalDateTime jobTaskInternalTime ) {
		this.jobTaskInternalTime = jobTaskInternalTime;
	}

	public String getJobParametersJSON() {
		return jobParametersJSON;
	}

	public void setJobParametersJSON( final String jobParametersJSON ) {
		this.jobParametersJSON = jobParametersJSON;
	}

	public String getJobResultJSON() {
		return jobResultJSON;
	}

	public void setJobResultJSON( final String jobResultJSON ) {
		this.jobResultJSON = jobResultJSON;
	}

	public JobExecutionState getJobExecutionState() {
		return jobExecutionState;
	}

	public void setJobExecutionState( final JobExecutionState jobExecutionState ) {
		this.jobExecutionState = jobExecutionState;
	}

	public JobTaskType getJobTaskType() {
		return jobTaskType;
	}

	public void setJobTaskType( final JobTaskType jobTaskType ) {
		this.jobTaskType = jobTaskType;
	}

	@Override
	public int hashCode() {
		return 31 * getId();
	}

	@Override
	public boolean equals( final Object obj ) {

		if ( obj == null ) {
			return false;
		}

		if ( obj == this ) {
			return true;
		}

		if ( !( obj instanceof JobLog ) ) {
			return false;
		}

		final JobLog jobLog = ( JobLog ) obj;
		return jobLog.getId() == getId();
	}

	@Override
	public String toString() {
		return String.format( "#%d: job task %d", getId(), jobTaskId );
	}
}
