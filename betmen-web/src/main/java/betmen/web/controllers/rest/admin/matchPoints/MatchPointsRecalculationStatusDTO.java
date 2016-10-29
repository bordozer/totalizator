package betmen.web.controllers.rest.admin.matchPoints;

import betmen.core.service.points.recalculation.MatchesPointsRecalculationMonitor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MatchPointsRecalculationStatusDTO {
    private final boolean matchPointsRecalculationInProgress;
    private final boolean brokenByUser;
    private final long totalSteps;
    private final long currentStep;

    public MatchPointsRecalculationStatusDTO(final MatchesPointsRecalculationMonitor monitor) {
        this.matchPointsRecalculationInProgress = monitor.isInProgress();
        this.brokenByUser = monitor.isBrokenByUser();
        this.totalSteps = monitor.getTotalSteps();
        this.currentStep = monitor.getCurrentStep();
    }
}
