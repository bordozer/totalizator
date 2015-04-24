package totalizator.app.models;

import javax.persistence.*;

import static totalizator.app.models.CupTeam.LOAD_ALL;
import static totalizator.app.models.CupTeam.LOAD_ALL_CUP_TEAMS;
import static totalizator.app.models.CupTeam.LOAD_CUP_TEAM;

@Entity
@Table( name = "cupTeams" )

@NamedQueries( {
		@NamedQuery(
				name = LOAD_ALL,
				query = "select c from CupTeam c"
		),
		@NamedQuery(
				name = LOAD_ALL_CUP_TEAMS,
				query = "select c from CupTeam c where cupId= :cupId"
		),
		@NamedQuery(
				name = LOAD_CUP_TEAM,
				query = "select c from CupTeam c where cupId= :cupId and teamId= :teamId"
		)
} )
public class CupTeam extends AbstractEntity {

	public static final String LOAD_ALL = "cupTeams.loadAll";
	public static final String LOAD_ALL_CUP_TEAMS = "cupTeams.loadAllForCup";
	public static final String LOAD_CUP_TEAM = "cupTeams.loadCupTeam";

	@ManyToOne
	@JoinColumn(name="cupId")
	private Cup cup;

	@ManyToOne
	@JoinColumn(name="teamId")
	private Team team;

	public CupTeam() {
	}

	public CupTeam( final Cup cup, final Team team ) {
		this.cup = cup;
		this.team = team;
	}

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

	@Override
	public String toString() {
		return String.format( "%s: %s", cup, team );
	}
}
