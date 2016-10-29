package betmen.core.service.jobs;

import betmen.core.entity.JobLog;
import betmen.core.service.GenericService;

import java.util.List;

public interface JobLogService extends GenericService<JobLog> {

    List<JobLog> loadAll(final int jobTaskId);
}
