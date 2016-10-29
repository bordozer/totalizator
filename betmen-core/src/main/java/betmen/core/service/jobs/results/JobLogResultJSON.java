package betmen.core.service.jobs.results;

public interface JobLogResultJSON {

    void setTotalSteps(final int totalSteps);

    int getTotalSteps();

    void setPerformedSteps(final int performedSteps);

    int getPerformedSteps();
}
