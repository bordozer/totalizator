package totalizator.app.models;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.time.LocalDateTime;

import static totalizator.app.models.Match.*;

@Entity
@Cacheable( true )
//@org.hibernate.annotations.Cache( region = "common", usage = CacheConcurrencyStrategy.READ_ONLY )
@Table( name = "matches" )
@NamedQueries( {
		@NamedQuery(
				name = LOAD_ALL,
				query = "select c from Match c order by cupId, beginningTime desc"
		),
		@NamedQuery(
				name = FIND_BY_CUP,
				query = "select c from Match c where cupId= :cupId order by beginningTime desc"
		),
		@NamedQuery(
				name = FIND_BY_TEAMS,
				query = "select c from Match c where ( team1Id= :team1Id and team2Id= :team2Id ) or (team1Id= :team2Id and team2Id= :team1Id) order by beginningTime desc"
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
	private int score1;

	@ManyToOne
	@JoinColumn(name="team2Id")
	private Team team2;
	private int score2;

	private LocalDateTime beginningTime;
	private boolean matchFinished;

	private int homeTeamNumber;

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

	public int getScore1() {
		return score1;
	}

	public void setScore1( final int score1 ) {
		this.score1 = score1;
	}

	public Team getTeam2() {
		return team2;
	}

	public void setTeam2( final Team team2 ) {
		this.team2 = team2;
	}

	public int getScore2() {
		return score2;
	}

	public void setScore2( final int score2 ) {
		this.score2 = score2;
	}

	public LocalDateTime getBeginningTime() {
		return beginningTime;
	}

	public void setBeginningTime( final LocalDateTime beginningTime ) {
		this.beginningTime = beginningTime;
	}

	public boolean isMatchFinished() {
		return matchFinished;
	}

	public void setMatchFinished( final boolean matchFinished ) {
		this.matchFinished = matchFinished;
	}

	public int getHomeTeamNumber() {
		return homeTeamNumber;
	}

	public void setHomeTeamNumber( final int homeTeamNumber ) {
		this.homeTeamNumber = homeTeamNumber;
	}

	@Override
	public String toString() {
		return String.format( "Match #%d: Team %s vs Team %s, %d : %d ( %s )", getId(), team1, team2, score1, score2, beginningTime );
	}
}
