package totalizator.app.models;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import totalizator.app.models.converters.JobTaskTypeConverter;
import totalizator.app.services.jobs.JobTaskType;

import javax.persistence.*;

import static totalizator.app.models.Job.LOAD_ALL;

@Entity
@org.hibernate.annotations.Cache( region = "common", usage = CacheConcurrencyStrategy.READ_WRITE )
@Table( name = "jobs" )
@NamedQueries( {
		@NamedQuery(
				name = LOAD_ALL,
				query = "select c from Job c order by jobName"
		)
} )
public class Job extends AbstractEntity {

	public static final java.lang.String LOAD_ALL = "jobs.loadAll";

	@Column( columnDefinition = "VARCHAR(255)" )
	private String jobName;

	@Column( columnDefinition = "INT(2)" )
	@Convert( converter = JobTaskTypeConverter.class )
	private JobTaskType jobTaskType;

	@Column( columnDefinition = "INT(1)" )
	private boolean jobTaskActive;

	@Column( columnDefinition = "VARCHAR(255)" )
	private String jobParametersJSON;

	public String getJobName() {
		return jobName;
	}

	public void setJobName( final String jobName ) {
		this.jobName = jobName;
	}

	public JobTaskType getJobTaskType() {
		return jobTaskType;
	}

	public void setJobTaskType( final JobTaskType jobTaskType ) {
		this.jobTaskType = jobTaskType;
	}

	public boolean isJobTaskActive() {
		return jobTaskActive;
	}

	public void setJobTaskActive( final boolean jobTaskActive ) {
		this.jobTaskActive = jobTaskActive;
	}

	public String getJobParametersJSON() {
		return jobParametersJSON;
	}

	public void setJobParametersJSON( final String jobParametersJSON ) {
		this.jobParametersJSON = jobParametersJSON;
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

		if ( !( obj instanceof Job ) ) {
			return false;
		}

		final Job job = ( Job ) obj;
		return job.getId() == getId();
	}
}
