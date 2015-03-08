package totalizator.app.models;

import javax.persistence.*;

import java.util.Date;

import static totalizator.app.models.MatchBet.*;

@Entity
@Table( name = "matchBets" )
@NamedQueries( {
		@NamedQuery(
				name = LOAD_ALL,
				query = "select mb from MatchBet mb order by betTime desc"
		),
		@NamedQuery(
				name = LOAD_FOR_USER,
				query = "select mb from MatchBet mb where userId= :userId order by betTime desc"
		),
		@NamedQuery(
				name = LOAD_FOR_MATCH,
				query = "select mb from MatchBet mb where matchId= :matchId order by betTime desc"
		),
		@NamedQuery(
				name = LOAD_FOR_USER_AND_MATCH,
				query = "select mb from MatchBet mb where userId= :userId and matchId= :matchId order by betTime desc"
		)
} )
public class MatchBet extends AbstractEntity {

	public static final String LOAD_ALL = "matchBets.loadAll";
	public static final String LOAD_FOR_USER = "matchBets.loadForUser";
	public static final String LOAD_FOR_MATCH = "matchBets.loadForMatch";
	public static final String LOAD_FOR_USER_AND_MATCH = "matchBets.loadForUserAndMatch";

	@ManyToOne
	@JoinColumn(name="matchId")
	private Match match;

	@ManyToOne
	@JoinColumn(name="userId")
	private User user;

	private int betScore1;
	private int betScore2;

	private Date betTime;

	public Match getMatch() {
		return match;
	}

	public void setMatch( final Match match ) {
		this.match = match;
	}

	public User getUser() {
		return user;
	}

	public void setUser( final User user ) {
		this.user = user;
	}

	public int getBetScore1() {
		return betScore1;
	}

	public void setBetScore1( final int betScore1 ) {
		this.betScore1 = betScore1;
	}

	public int getBetScore2() {
		return betScore2;
	}

	public void setBetScore2( final int betScore2 ) {
		this.betScore2 = betScore2;
	}

	public Date getBetTime() {
		return betTime;
	}

	public void setBetTime( final Date betTime ) {
		this.betTime = betTime;
	}

	@Override
	public String toString() {
		return String.format( "%s: %d - %d ( %s )", match, betScore1, betScore2, user );
	}
}