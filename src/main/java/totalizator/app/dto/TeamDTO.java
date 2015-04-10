package totalizator.app.dto;

public class TeamDTO {

	private int teamId;
	private String teamName;
	private CategoryDTO category;
	private String teamLogo;

	public TeamDTO() {
	}

	public TeamDTO( final int teamId, final String teamName, final CategoryDTO category, final String teamLogo ) {
		this.teamId = teamId;
		this.teamName = teamName;
		this.category = category;
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

	public CategoryDTO getCategory() {
		return category;
	}

	public void setCategory( final CategoryDTO category ) {
		this.category = category;
	}

	public String getTeamLogo() {
		return teamLogo;
	}

	public void setTeamLogo( final String teamLogo ) {
		this.teamLogo = teamLogo;
	}
}
