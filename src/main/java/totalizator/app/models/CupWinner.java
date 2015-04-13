package totalizator.app.models;

import javax.persistence.*;

import static totalizator.app.models.CupWinner.*;

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

	private int cupPosition;

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

	public int getCupPosition() {
		return cupPosition;
	}

	public void setCupPosition( final int cupPosition ) {
		this.cupPosition = cupPosition;
	}

	@Override
	public String toString() {
		return String.format( "%s: %s %d", cup, team, cupPosition );
	}
}
