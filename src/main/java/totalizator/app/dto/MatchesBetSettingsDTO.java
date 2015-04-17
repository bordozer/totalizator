package totalizator.app.dto;

public class MatchesBetSettingsDTO {

	private int userId;
	private int categoryId;
	private int cupId;
	private int teamId;
	private int team2Id;
	private boolean showFutureMatches;
	private boolean showFinished;

	public int getUserId() {
		return userId;
	}

	public void setUserId( final int userId ) {
		this.userId = userId;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId( final int categoryId ) {
		this.categoryId = categoryId;
	}

	public int getCupId() {
		return cupId;
	}

	public void setCupId( final int cupId ) {
		this.cupId = cupId;
	}

	public int getTeamId() {
		return teamId;
	}

	public void setTeamId( final int teamId ) {
		this.teamId = teamId;
	}

	public int getTeam2Id() {
		return team2Id;
	}

	public void setTeam2Id( final int team2Id ) {
		this.team2Id = team2Id;
	}

	public boolean isShowFutureMatches() {
		return showFutureMatches;
	}

	public void setShowFutureMatches( final boolean showFutureMatches ) {
		this.showFutureMatches = showFutureMatches;
	}

	public boolean isShowFinished() {
		return showFinished;
	}

	public void setShowFinished( final boolean showFinished ) {
		this.showFinished = showFinished;
	}

	@Override
	public String toString() {
		return String.format( "User: %d, Category: %d; cup: %d; team: %d", userId, categoryId, cupId, teamId );
	}
}
