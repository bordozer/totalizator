package totalizator.app.services.jobs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import totalizator.app.dao.JobsDao;
import totalizator.app.models.Job;
import totalizator.app.services.utils.JsonService;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JobTaskServiceImpl implements JobTaskService {

	@Autowired
	private JobsDao jobsRepository;

	@Autowired
	private JsonService jsonService;

	@Override
	public List<JobTask> loadAll() {

		return jobsRepository.loadAll()
				.stream()
				.map( fromDBMapper() )
				.collect( Collectors.toList() );
	}

	@Override
	@Transactional( readOnly = true )
	public JobTask load( final int id ) {
		return fromDBMapper().apply( jobsRepository.load( id ) );
	}

	@Override
	@Transactional
	public JobTask save( final JobTask jobTask ) {
		return fromDBMapper().apply( jobsRepository.save( toDBMapper().apply( jobTask ) ) );
	}

	@Override
	@Transactional
	public void delete( final int id ) {
		jobsRepository.delete( id );
	}

	private Function<Job, JobTask> fromDBMapper() {

		return new Function<Job, JobTask>() {

			@Override
			public JobTask apply( final Job job ) {

				final JobTask jobTask = new JobTask();

				jobTask.setId( job.getId() );

				jobTask.setJobName( job.getJobName() );
				jobTask.setJobTaskType( job.getJobTaskType() );
				jobTask.setJobTaskActive( job.isJobTaskActive() );

				initJobTaskParameters( jobTask, job );

				return jobTask;
			}

			private void initJobTaskParameters( final JobTask jobTask, final Job job ) {

				switch ( job.getJobTaskType() ) {
					case IMPORT_REMOTE_GAMES:
						jobTask.setJobTaskParameters( jsonService.fromJson( job.getJobParametersJSON() ) );
						return;
				}

				throw new IllegalArgumentException( String.format( "Unsupported JobType: %s", job.getJobTaskType() ) );
			}
		};
	}

	private Function<JobTask, Job> toDBMapper() {

		return new Function<JobTask, Job>() {

			@Override
			public Job apply( final JobTask jobTask ) {

				final Job job = getJobDBEntry( jobTask.getId() );

				job.setJobName( jobTask.getJobName() );
				job.setJobTaskType( jobTask.getJobTaskType() );
				job.setJobTaskActive( jobTask.isJobTaskActive() );
				job.setJobParametersJSON( jsonService.toJson( jobTask.getJobTaskParameters() ) );

				return job;
			}

			private Job getJobDBEntry( final int jobTaskId ) {

				if ( jobTaskId == 0 ) {
					return new Job();
				}

				return jobsRepository.load( jobTaskId );
			}
		};
	}
}
