package totalizator.app.models;

import totalizator.app.enums.TeamBetEventType;

import javax.persistence.*;

import static totalizator.app.models.TeamBet.LOAD_ALL_FOR_CUP;
import static totalizator.app.models.TeamBet.LOAD_ALL_FOR_CUP_AND_USER;

@Entity
@Table(name = "teamBets")
@NamedQueries( {
		@NamedQuery(
				name = LOAD_ALL_FOR_CUP,
				query = "select t from TeamBet t where cupId= :cupId"
		),
		@NamedQuery(
				name = LOAD_ALL_FOR_CUP_AND_USER,
				query = "select t from TeamBet t where cupId= :cupId and userId= :userId"
		)
} )
public class TeamBet extends AbstractEntity {

	public static final String LOAD_ALL_FOR_CUP = "teamBet.loadForCup";
	public static final String LOAD_ALL_FOR_CUP_AND_USER = "teamBet.loadForCupCupAndUser";

	@ManyToOne
	@JoinColumn(name="cupId")
	private Cup cup;

	@ManyToOne
	@JoinColumn(name="userId")
	private User user;

	private TeamBetEventType teamBetEventType;

	public Cup getCup() {
		return cup;
	}

	public void setCup( final Cup cup ) {
		this.cup = cup;
	}

	public User getUser() {
		return user;
	}

	public void setUser( final User user ) {
		this.user = user;
	}

	public TeamBetEventType getTeamBetEventType() {
		return teamBetEventType;
	}

	public void setTeamBetEventType( final TeamBetEventType teamBetEventType ) {
		this.teamBetEventType = teamBetEventType;
	}
}
