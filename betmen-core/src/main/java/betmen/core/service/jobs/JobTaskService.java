package betmen.core.service.jobs;

import java.util.List;

public interface JobTaskService {

    List<JobTask> loadAll();

    JobTask load(final int id);

    JobTask save(final JobTask entry);

    void delete(final int id);
}
