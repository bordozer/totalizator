package totalizator.app.services.jobs;

import totalizator.app.models.JobLog;
import totalizator.app.services.GenericService;

import java.util.List;

public interface JobLogService extends GenericService<JobLog> {

	List<JobLog> loadAll( final int jobTaskId );
}
