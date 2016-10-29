package betmen.core.service.jobs.execution;

import betmen.core.service.jobs.executors.AbstractJobTaskExecutor;

import java.time.LocalDateTime;

public interface JobTaskExecutionService {

    void execute(final int jobTaskId);

    void execute(final int jobTaskId, final LocalDateTime jobTaskInternalTime);

    void stop(int jobTaskId);

    AbstractJobTaskExecutor getJobExecutor(final int jobTaskId);

    boolean isJobExecutingNow(final int jobTaskId);
}
