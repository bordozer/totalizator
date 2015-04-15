package totalizator.app.controllers.rest.cupTeams;

import totalizator.app.dto.TeamDTO;

import java.util.List;
import java.util.Set;

public class CupTeamsDTO {

	private List<TeamDTO> teams;
	private Set<String> letters;

	public CupTeamsDTO( final List<TeamDTO> teams, final Set<String> letters ) {
		this.teams = teams;
		this.letters = letters;
	}

	public List<TeamDTO> getTeams() {
		return teams;
	}

	public void setTeams( final List<TeamDTO> teams ) {
		this.teams = teams;
	}

	public Set<String> getLetters() {
		return letters;
	}

	public void setLetters( final Set<String> letters ) {
		this.letters = letters;
	}
}
