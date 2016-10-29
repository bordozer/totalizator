package betmen.core.repository;

import betmen.core.entity.JobLog;
import betmen.core.service.GenericService;

import java.util.List;

public interface JobLogDao extends GenericService<JobLog> {

    String CACHE_ENTRY = "totalizator.app.cache.jobLog";
    String CACHE_QUERY = "totalizator.app.cache.jobLogs";

    List<JobLog> loadAll(final int jobTaskId);
}
