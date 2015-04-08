package totalizator.app.dto;

public class CupTeamBetDTO {

	private CupDTO cup;
	private TeamDTO team;
	private UserDTO user;

	public CupDTO getCup() {
		return cup;
	}

	public void setCup( final CupDTO cup ) {
		this.cup = cup;
	}

	public TeamDTO getTeam() {
		return team;
	}

	public void setTeam( final TeamDTO team ) {
		this.team = team;
	}

	public UserDTO getUser() {
		return user;
	}

	public void setUser( final UserDTO user ) {
		this.user = user;
	}
}