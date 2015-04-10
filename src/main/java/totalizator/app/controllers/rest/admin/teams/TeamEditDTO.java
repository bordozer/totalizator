package totalizator.app.controllers.rest.admin.teams;

public class TeamEditDTO {

	private int teamId;
	private String teamName;
	private int categoryId;
	private String teamLogo;

	public TeamEditDTO() {
	}

	public TeamEditDTO( final int teamId, final String teamName, final int categoryId, final String teamLogo ) {
		this.teamId = teamId;
		this.teamName = teamName;
		this.categoryId = categoryId;
		this.teamLogo = teamLogo;
	}

	public int getTeamId() {
		return teamId;
	}

	public void setTeamId( final int teamId ) {
		this.teamId = teamId;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName( final String teamName ) {
		this.teamName = teamName;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId( final int categoryId ) {
		this.categoryId = categoryId;
	}

	public String getTeamLogo() {
		return teamLogo;
	}

	public void setTeamLogo( final String teamLogo ) {
		this.teamLogo = teamLogo;
	}

	@Override
	public String toString() {
		return String.format( "%d: %s", teamId, teamName );
	}
}
