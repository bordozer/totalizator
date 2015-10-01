package totalizator.app.dao;

import totalizator.app.models.JobLog;
import totalizator.app.services.GenericService;

import java.util.List;

public interface JobLogDao extends GenericService<JobLog> {

	String CACHE_ENTRY = "totalizator.app.cache.jobLog";
	String CACHE_QUERY = "totalizator.app.cache.jobLogs";

	List<JobLog> loadAll( final int jobTaskId );
}
