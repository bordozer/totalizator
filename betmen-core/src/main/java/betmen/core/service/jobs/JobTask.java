package betmen.core.service.jobs;

import betmen.core.service.jobs.parameters.AbstractJobTaskParameters;

public class JobTask {

    private int id;
    private String jobName;
    private JobTaskType jobTaskType;
    private boolean jobTaskActive;

    private AbstractJobTaskParameters jobTaskParameters;

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(final String jobName) {
        this.jobName = jobName;
    }

    public JobTaskType getJobTaskType() {
        return jobTaskType;
    }

    public void setJobTaskType(final JobTaskType jobTaskType) {
        this.jobTaskType = jobTaskType;
    }

    public boolean isJobTaskActive() {
        return jobTaskActive;
    }

    public void setJobTaskActive(final boolean jobTaskActive) {
        this.jobTaskActive = jobTaskActive;
    }

    public AbstractJobTaskParameters getJobTaskParameters() {
        return jobTaskParameters;
    }

    public void setJobTaskParameters(final AbstractJobTaskParameters jobTaskParameters) {
        this.jobTaskParameters = jobTaskParameters;
    }

    @Override
    public int hashCode() {
        return 31 * getId();
    }

    @Override
    public boolean equals(final Object obj) {

        if (obj == null) {
            return false;
        }

        if (obj == this) {
            return true;
        }

        if (!(obj.getClass().equals(getClass()))) {
            return false;
        }

        final JobTask job = (JobTask) obj;
        return job.getId() == getId();
    }

    @Override
    public String toString() {
        return String.format("#%d: %s", id, jobName);
    }
}
