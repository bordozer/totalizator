package totalizator.app.models;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import totalizator.app.models.converters.LocalDateTimeConverter;

import javax.persistence.*;
import java.time.LocalDateTime;

import static totalizator.app.models.MatchBet.*;

@Entity
@org.hibernate.annotations.Cache( region = "common", usage = CacheConcurrencyStrategy.READ_WRITE )
@Table(
		name = "matchBets"
		, indexes = {
			@Index( name = "matchId_userId_idx", columnList = "matchId,userId", unique = true )
		}
)
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
		),
		@NamedQuery(
				name = LOAD_MATCH_BETS_COUNT,
				query = "select count( id ) from MatchBet mb where matchId= :matchId"
		),
		@NamedQuery(
				name = LOAD_COUNT_OF_CUP_MATCHES_WITH_USER_BET,
				query = "select count( mb.id ) from MatchBet as mb join mb.match as m where mb.user.id = :userId and m.cup.id = :cupId"
		),
		@NamedQuery(
				name = LOAD_COUNT_OF_CUP_MATCHES_ACCESSIBLE_FOR_BETTING_FOR_USER,
				query = "select count( m.id ) from Match as m where m.cup.id = :cupId and m.beginningTime >= :time and m.id not in ( select mb.match.id from MatchBet mb where mb.user.id = :userId )"
		)
} )
public class MatchBet extends AbstractEntity {

	public static final String LOAD_ALL = "matchBets.loadAll";
	public static final String LOAD_FOR_USER = "matchBets.loadForUser";
	public static final String LOAD_FOR_MATCH = "matchBets.loadForMatch";
	public static final String LOAD_FOR_USER_AND_MATCH = "matchBets.loadForUserAndMatch";
	public static final String LOAD_MATCH_BETS_COUNT = "matchBets.matchBetsCount";

	public static final String LOAD_COUNT_OF_CUP_MATCHES_WITH_USER_BET = "matchBets.LOAD_COUNT_OF_CUP_MATCHES_WITH_USER_BET";
	public static final String LOAD_COUNT_OF_CUP_MATCHES_ACCESSIBLE_FOR_BETTING_FOR_USER = "matchBets.LOAD_COUNT_OF_CUP_MATCHES_ACCESSIBLE_FOR_BETTING_FOR_USER";

	@ManyToOne
	@JoinColumn(name="matchId")
	private Match match;

	@ManyToOne
	@JoinColumn(name="userId")
	private User user;

	private int betScore1;
	private int betScore2;

	@Convert( converter = LocalDateTimeConverter.class )
	private LocalDateTime betTime;

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

	public LocalDateTime getBetTime() {
		return betTime;
	}

	public void setBetTime( final LocalDateTime betTime ) {
		this.betTime = betTime;
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

		if ( !( obj instanceof MatchBet ) ) {
			return false;
		}

		final MatchBet matchBet = ( MatchBet ) obj;
		return matchBet.getId() == getId();
	}

	@Override
	public String toString() {
		return String.format( "%s: %d - %d ( %s )", match, betScore1, betScore2, user );
	}
}
