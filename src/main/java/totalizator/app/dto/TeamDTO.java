package totalizator.app.dto;

public class TeamDTO {

	private int teamId;
	private String teamName;
	private int categoryId;

	public TeamDTO() {
	}

	public TeamDTO( final int teamId, final String teamName, final int categoryId ) {
		this.teamId = teamId;
		this.teamName = teamName;
		this.categoryId = categoryId;
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
}
