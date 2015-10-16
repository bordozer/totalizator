package totalizator.app.controllers.rest.matchPoints;

import totalizator.app.services.points.recalculation.MatchesPointsRecalculationMonitor;

public class MatchPointsRecalculationStatusDTO {

	private final boolean matchPointsRecalculationInProgress;
	private final boolean brokenByUser;

	private final long totalSteps;
	private final long currentStep;

	public MatchPointsRecalculationStatusDTO( final MatchesPointsRecalculationMonitor monitor ) {

		this.matchPointsRecalculationInProgress = monitor.isInProgress();
		this.brokenByUser = monitor.isBrokenByUser();

		this.totalSteps = monitor.getTotalSteps();
		this.currentStep = monitor.getCurrentStep();
	}

	public boolean isMatchPointsRecalculationInProgress() {
		return matchPointsRecalculationInProgress;
	}

	public boolean isBrokenByUser() {
		return brokenByUser;
	}

	public long getTotalSteps() {
		return totalSteps;
	}

	public long getCurrentStep() {
		return currentStep;
	}
}
