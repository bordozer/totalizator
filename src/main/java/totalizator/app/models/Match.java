package totalizator.app.models;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;

import static totalizator.app.models.Match.*;

@Entity
@org.hibernate.annotations.Cache( region = "common", usage = CacheConcurrencyStrategy.READ_WRITE )
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
				name = FIND_BY_TEAM,
				query = "select c from Match c where ( team1Id= :teamId ) or ( team2Id= :teamId ) order by beginningTime desc"
		),
		@NamedQuery(
				name = FIND_BY_TEAMS,
				query = "select c from Match c where ( team1Id= :team1Id and team2Id= :team2Id ) or (team1Id= :team2Id and team2Id= :team1Id) order by beginningTime desc"
		),
		@NamedQuery(
				name = FIND_BY_CUP_AND_TEAMS,
				query = "select c from Match c where ( cupId= :cupId ) and  ( ( team1Id= :team1Id and team2Id= :team2Id ) or ( team1Id= :team2Id and team2Id= :team1Id ) ) order by beginningTime desc"
		),
		@NamedQuery(
				name = FIND_ALL_TEAM_MATCHES_FOR_CUP,
				query = "select c from Match c where ( cupId= :cupId ) and ( team1Id= :teamId or team2Id= :teamId ) order by beginningTime desc"
		),
		@NamedQuery(
				name = FIND_MATCH_BY_REMOTE_GAME_ID,
				query = "select c from Match c where remoteGameId= :remoteGameId"
		),
		@NamedQuery(
				name = LOAD_MATCH_COUNT_FOR_CUP,
				query = "select count(m) from Match m where cupId= :cupId"
		),
		@NamedQuery(
				name = LOAD_MATCH_COUNT_FOR_CUP_AND_TEAM,
				query = "select count(m) from Match m where ( cupId= :cupId ) and ( team1Id= :teamId or team2Id= :teamId )"
		),
		@NamedQuery(
				name = LOAD_MATCH_COUNT_FOR_TEAM,
				query = "select count(m) from Match m where ( team1Id= :teamId or team2Id= :teamId )"
		),
		@NamedQuery(
				name = LOAD_FINISHED_MATCH_COUNT_FOR_CUP_AND_TEAM,
				query = "select count(m) from Match m where ( cupId= :cupId ) and ( matchFinished = true ) and ( team1Id= :teamId or team2Id= :teamId )"
		),
		@NamedQuery(
				name = LOAD_FUTURE_MATCH_COUNT_FOR_CUP_AND_TEAM,
				query = "select count(m) from Match m where ( cupId= :cupId ) and ( matchFinished = false ) and ( team1Id= :teamId or team2Id= :teamId )"
		),
		@NamedQuery(
				name = FIND_MATCHES_BY_DATE,
				query = "select m from Match m where beginningTime BETWEEN :timeFrom AND :timeTo"
		)
} )
public class Match extends AbstractEntity {

	public static final String LOAD_ALL = "matches.loadAll";
	public static final String FIND_BY_CUP = "matches.findByCup";
	public static final String FIND_BY_TEAM = "matches.findByTeam";
	public static final String FIND_BY_TEAMS = "matches.findByTeams";
	public static final String FIND_BY_CUP_AND_TEAMS = "matches.findByCupAndTeams";
	public static final String FIND_ALL_TEAM_MATCHES_FOR_CUP = "matches.findByCupAndTeam";
	public static final String FIND_MATCH_BY_REMOTE_GAME_ID = "matches.findByRemoteGameId";
	public static final String LOAD_MATCH_COUNT_FOR_CUP = "cups.loadCupMatchCount";
	public static final String LOAD_MATCH_COUNT_FOR_CUP_AND_TEAM = "cups.loadTeamMatchCountForCup";
	public static final String LOAD_MATCH_COUNT_FOR_TEAM = "cups.loadTeamMatchCount";
	public static final String LOAD_FINISHED_MATCH_COUNT_FOR_CUP_AND_TEAM = "cups.finishedMatchCountForCupAndTeam";
	public static final String LOAD_FUTURE_MATCH_COUNT_FOR_CUP_AND_TEAM = "cups.futureMatchCountForCupAndTeam";
	public static final String FIND_MATCHES_BY_DATE = "cups.futureMatchByDate";

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

	@Column( columnDefinition = "VARCHAR(255)" ) // TODO: is too small for real description, but hsqldb:mem:mydb does not have TEXT data type. Just a temporary solution
	private String description;

	@Column( columnDefinition = "VARCHAR(100)" )
	private String remoteGameId;

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

	public String getDescription() {
		return description;
	}

	public void setDescription( final String description ) {
		this.description = description;
	}

	public String getRemoteGameId() {
		return remoteGameId;
	}

	public void setRemoteGameId( final String remoteGameId ) {
		this.remoteGameId = remoteGameId;
	}

	@Override
	public int hashCode() {
		return 31 * getId();
	}

	@Override
	public boolean equals( final Object obj ) {

		if ( obj == null ) {
			return false;
		}

		if ( obj == this ) {
			return true;
		}

		if ( !( obj instanceof Match ) ) {
			return false;
		}

		final Match match = ( Match ) obj;
		return match.getId() == getId();
	}

	@Override
	public String toString() {
		return String.format( "Match #%d: Team %s vs Team %s, %d : %d ( %s )", getId(), team1, team2, score1, score2, beginningTime );
	}
}
