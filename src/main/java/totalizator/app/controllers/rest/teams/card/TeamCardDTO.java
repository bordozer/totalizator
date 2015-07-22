package totalizator.app.controllers.rest.teams.card;

import totalizator.app.dto.TeamDTO;

import java.util.List;

public class TeamCardDTO {

	private TeamDTO team;
	private List<TeamCardCupData> cardCupData;

	public TeamDTO getTeam() {
		return team;
	}
	public void setTeam( final TeamDTO team ) {
		this.team = team;
	}

	public List<TeamCardCupData> getCardCupData() {
		return cardCupData;
	}

	public void setCardCupData( final List<TeamCardCupData> cardCupData ) {
		this.cardCupData = cardCupData;
	}
}
