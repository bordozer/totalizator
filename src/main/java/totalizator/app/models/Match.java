package totalizator.app.models;

import javax.persistence.*;
import java.util.Date;

import static totalizator.app.models.Match.*;

@Entity
@Table( name = "matches" )
@NamedQueries( {
		@NamedQuery(
				name = LOAD_ALL,
				query = "select c from Match c order by cupId"
		),
		@NamedQuery(
				name = FIND_BY_CUP,
				query = "select c from Match c where cupId= :cupId"
		),
		@NamedQuery(
				name = FIND_BY_TEAMS,
				query = "select c from Match c where team1Id= :team1Id and team2Id= :team2Id"
		)
} )
public class Match extends AbstractEntity {

	public static final String LOAD_ALL = "matches.loadAll";
	public static final String FIND_BY_CUP = "matches.findByCup";
	public static final String FIND_BY_TEAMS = "matches.findByTeams";

	@ManyToOne
	@JoinColumn(name="cupId")
	private Cup cup;

	@ManyToOne
	@JoinColumn(name="team1Id")
	private Team team1;
	private int score1Id;

	@ManyToOne
	@JoinColumn(name="team2Id")
	private Team team2;
	private int score2Id;

	private Date lastBetTime;

	public Cup getCup() {
		return cup;
	}

	public void setCup( final Cup cup ) {
		this.cup = cup;
	}

	public Team getTeam1() {
		return team1;
	}

	public void setTeam1( final Team team1 ) {
		this.team1 = team1;
	}

	public int getScore1Id() {
		return score1Id;
	}

	public void setScore1Id( final int score1Id ) {
		this.score1Id = score1Id;
	}

	public Team getTeam2() {
		return team2;
	}

	public void setTeam2( final Team team2 ) {
		this.team2 = team2;
	}

	public int getScore2Id() {
		return score2Id;
	}

	public void setScore2Id( final int score2Id ) {
		this.score2Id = score2Id;
	}

	public Date getLastBetTime() {
		return lastBetTime;
	}

	public void setLastBetTime( final Date lastBetTime ) {
		this.lastBetTime = lastBetTime;
	}

	@Override
	public String toString() {
		return String.format( "%s vs %s, %d : %d", team1, team2, score1Id, score2Id );
	}
}
