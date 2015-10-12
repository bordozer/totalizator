package totalizator.app.controllers.rest.admin.jobs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import totalizator.app.services.jobs.jobDTO.JobDTOService;
import totalizator.app.services.jobs.JobTask;
import totalizator.app.services.jobs.JobTaskService;
import totalizator.app.services.jobs.JobTaskType;
import totalizator.app.services.jobs.execution.JobTaskExecutionService;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping( "/admin/rest/jobs" )
public class AdminJobEditRestController {

	@Autowired
	private JobTaskService jobTaskService;

	@Autowired
	private JobTaskExecutionService jobTaskExecutionService;

	@Autowired
	private JobDTOService jobDTOService;

	@RequestMapping( method = RequestMethod.GET, value = "/" )
	public List<JobTaskIdDTO> getJobTaskIds() {

		return jobTaskService.loadAll()
				.stream()
				.map( new Function<JobTask, JobTaskIdDTO>() {
					@Override
					public JobTaskIdDTO apply( final JobTask jobTask ) {
						return new JobTaskIdDTO( jobTask.getId(), jobTask.getJobTaskType().getId() );
					}
				} )
				.collect( Collectors.toList() );
	}

	@RequestMapping( method = RequestMethod.GET, value = "/{jobTaskTypeId}/{jobTaskId}/" )
	public JobTaskEditDTO getJobTask( final @PathVariable int jobTaskTypeId, final @PathVariable int jobTaskId ) {
		return loadJobTask( jobTaskService.load( jobTaskId ) );
	}

	@RequestMapping( method = RequestMethod.PUT, value = "/1/0" )
	public JobTaskEditDTO create( final @RequestBody JobTaskGamesImportEditDTO dto ) {
		return initFromDtoSaveAndReturn( dto, new JobTask() );
	}

	@RequestMapping( method = RequestMethod.PUT, value = "/1/{jobId}" )
	public JobTaskEditDTO save( final @RequestBody JobTaskGamesImportEditDTO dto ) {
		return initFromDtoSaveAndReturn( dto, jobTaskService.load( dto.getJobTaskId() ) );
	}

	@RequestMapping( method = RequestMethod.DELETE, value = "/{jobTaskTypeId}/{jobTaskId}" )
	public void delete( final @PathVariable int jobTaskTypeId, final @PathVariable int jobTaskId ) {

		if ( jobTaskId == 0 ) {
			return;
		}

		jobTaskService.delete( jobTaskId );
	}

	private JobTaskEditDTO initFromDtoSaveAndReturn( final @RequestBody JobTaskGamesImportEditDTO dto, final JobTask jobTask ) {

		initFromDTO( dto, jobTask );

		return loadJobTask( jobTaskService.save( jobTask ) );
	}

	private JobTaskEditDTO loadJobTask( final JobTask load ) {
		return toDTOMapper().apply( load );
	}

	private void initFromDTO( final JobTaskEditDTO dto, final JobTask jobTask ) {

		final JobTaskType jobTaskType = JobTaskType.getById( dto.getJobTaskTypeId() );

		jobTask.setJobName( dto.getJobTaskName() );
		jobTask.setJobTaskType( jobTaskType );
		jobTask.setJobTaskActive( dto.isJobTaskActive() );
		jobTask.setJobTaskParameters( jobDTOService.transformParametersFromDTO( jobTaskType, dto.getJobTaskParametersHolder() ) );
	}

	private Function<JobTask, JobTaskEditDTO> toDTOMapper() {

		return new Function<JobTask, JobTaskEditDTO>() {

			@Override
			public JobTaskEditDTO apply( final JobTask jobTask ) {

				final JobTaskEditDTO dto = new JobTaskEditDTO();

				dto.setJobTaskId( jobTask.getId() );
				dto.setJobTaskName( jobTask.getJobName() );
				dto.setJobTaskTypeId( jobTask.getJobTaskType().getId() );
				dto.setJobTaskActive( jobTask.isJobTaskActive() );

				dto.setJobTaskParametersHolder( jobDTOService.transformParametersToDTO( jobTask.getJobTaskType(), jobTask.getJobTaskParameters() ) );

				dto.setJobTaskExecuting( jobTaskExecutionService.isJobExecutingNow( jobTask.getId() ) );

				return dto;
			}
		};
	}
}
