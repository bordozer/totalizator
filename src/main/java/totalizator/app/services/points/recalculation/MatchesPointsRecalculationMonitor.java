package totalizator.app.services.points.recalculation;

public final class MatchesPointsRecalculationMonitor {

	private boolean inProgress;
	private boolean brokenByUser;

	private long totalSteps;
	private long currentStep;

	public boolean isInProgress() {
		return inProgress;
	}

	public void setInProgress( final boolean inProgress ) {
		this.inProgress = inProgress;
	}

	public boolean isBrokenByUser() {
		return brokenByUser;
	}

	public void setBrokenByUser( final boolean brokenByUser ) {
		this.brokenByUser = brokenByUser;
	}

	public long getTotalSteps() {
		return totalSteps;
	}

	public void setTotalSteps( final long totalSteps ) {
		this.totalSteps = totalSteps;
	}

	public long getCurrentStep() {
		return currentStep;
	}

	public void setCurrentStep( final long currentStep ) {
		this.currentStep = currentStep;
	}

	public void reset() {
		inProgress = false;
		brokenByUser = false;
		totalSteps = 0;
		currentStep = 0;
	}

	public void startProgress() {
		inProgress = true;
	}

	public void increase() {
		currentStep++;
	}
}
