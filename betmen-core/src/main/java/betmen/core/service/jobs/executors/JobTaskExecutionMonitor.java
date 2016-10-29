package betmen.core.service.jobs.executors;

import java.time.LocalDateTime;

public class JobTaskExecutionMonitor {

    private int totalSteps;
    private int currentStep;

    private final LocalDateTime startTime;
    private LocalDateTime finishingTime;

    public JobTaskExecutionMonitor(final LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public int getTotalSteps() {
        return totalSteps;
    }

    public void setTotalSteps(final int totalSteps) {
        this.totalSteps = totalSteps;
    }

    public int getCurrentStep() {
        return currentStep;
    }

    public void setCurrentStep(final int currentStep) {
        this.currentStep = currentStep;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void increase() {
        currentStep++;
    }

    public LocalDateTime getFinishingTime() {
        return finishingTime;
    }

    public void setFinishingTime(final LocalDateTime finishingTime) {
        this.finishingTime = finishingTime;
    }
}
