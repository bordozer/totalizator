package totalizator.app.models;

import totalizator.app.enums.CupPosition;

import javax.persistence.*;

import static totalizator.app.models.CupWinner.LOAD_ALL;
import static totalizator.app.models.CupWinner.LOAD_FOR_CUP;
import static totalizator.app.models.CupWinner.LOAD_FOR_CUP_AND_TEAM;

@Entity
@Table(
		name = "cupWinners"
)
@NamedQueries( {
		@NamedQuery(
				name = LOAD_ALL,
				query = "select t from CupWinner t order by cupPosition desc"
		),
		@NamedQuery(
				name = LOAD_FOR_CUP,
				query = "select t from CupWinner t where cupId= :cupId order by cupPosition desc"
		),
		@NamedQuery(
				name = LOAD_FOR_CUP_AND_TEAM,
				query = "select t from CupWinner t where cupId= :cupId and teamId= :teamId"
		)
} )
public class CupWinner extends AbstractEntity {

	public static final String LOAD_ALL = "cupWinners.loadAll";
	public static final String LOAD_FOR_CUP = "cupWinners.loadForCup";
	public static final String LOAD_FOR_CUP_AND_TEAM = "cupWinners.loadForCupAndTeam";

	@ManyToOne
	@JoinColumn(name="cupId")
	private Cup cup;

	@ManyToOne
	@JoinColumn(name="teamId")
	private Team team;

	private CupPosition cupPosition;

	public Cup getCup() {
		return cup;
	}

	public void setCup( final Cup cup ) {
		this.cup = cup;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam( final Team team ) {
		this.team = team;
	}

	public CupPosition getCupPosition() {
		return cupPosition;
	}

	public void setCupPosition( final CupPosition cupPosition ) {
		this.cupPosition = cupPosition;
	}

	@Override
	public String toString() {
		return String.format( "%s: %s %s", cup, team, cupPosition );
	}
}
