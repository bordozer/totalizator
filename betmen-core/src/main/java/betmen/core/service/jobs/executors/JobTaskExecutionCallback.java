package betmen.core.service.jobs.executors;

public abstract class JobTaskExecutionCallback {

    protected GamesImportJobTaskExecutor jobTaskExecutor;

    public abstract void afterJobDone();

    public JobTaskExecutionCallback(final GamesImportJobTaskExecutor jobTaskExecutor) {
        this.jobTaskExecutor = jobTaskExecutor;
    }
}
