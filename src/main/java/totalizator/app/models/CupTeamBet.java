package totalizator.app.models;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import totalizator.app.models.converters.LocalDateTimeConverter;

import javax.persistence.*;
import java.time.LocalDateTime;

import static totalizator.app.models.CupTeamBet.*;

@Entity
@org.hibernate.annotations.Cache( region = "common", usage = CacheConcurrencyStrategy.READ_WRITE )
@Table(
	name = "cupTeamBets"
)
@NamedQueries( {
		@NamedQuery(
				name = LOAD_ALL,
				query = "select t from CupTeamBet t order by betTime desc"
		),
		@NamedQuery(
				name = LOAD_ALL_FOR_CUP,
				query = "select t from CupTeamBet t where cupId= :cupId order by betTime desc"
		),
		@NamedQuery(
				name = LOAD_ALL_FOR_CUP_AND_USER,
				query = "select t from CupTeamBet t where cupId= :cupId and userId= :userId order by cupPosition"
		),
		@NamedQuery(
				name = LOAD_ALL_FOR_CUP_AND_TEAM_AND_USER,
				query = "select t from CupTeamBet t where cupId= :cupId and teamId= :teamId and userId= :userId order by betTime desc"
		),
		@NamedQuery(
				name = LOAD_ALL_FOR_CUP_AND_USER_AND_POSITION,
				query = "select t from CupTeamBet t where cupId= :cupId and userId= :userId and cupPosition= :cupPosition"
		)
} )
public class CupTeamBet extends AbstractEntity {

	public static final String LOAD_ALL = "cupTeamBets.loadAll";
	public static final String LOAD_ALL_FOR_CUP = "cupTeamBets.loadForCup";
	public static final String LOAD_ALL_FOR_CUP_AND_USER = "cupTeamBets.loadForCupAndUser";
	public static final String LOAD_ALL_FOR_CUP_AND_TEAM_AND_USER = "cupTeamBets.loadForCupAndTeamAndUser";
	public static final String LOAD_ALL_FOR_CUP_AND_USER_AND_POSITION = "cupTeamBets.loadForCupAndUserAndPosition";

	@ManyToOne
	@JoinColumn(name="cupId")
	private Cup cup;

	@ManyToOne
	@JoinColumn(name="teamId")
	private Team team;

	@ManyToOne
	@JoinColumn(name="userId")
	private User user;

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

	public User getUser() {
		return user;
	}

	public void setUser( final User user ) {
		this.user = user;
	}

	public int getCupPosition() {
		return cupPosition;
	}

	public void setCupPosition( final int cupPosition ) {
		this.cupPosition = cupPosition;
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

		if ( !( obj instanceof CupTeamBet ) ) {
			return false;
		}

		final CupTeamBet cupWinner = ( CupTeamBet ) obj;
		return cupWinner.getId() == getId();
	}

	@Override
	public String toString() {
		return String.format( "%s: %s will get %s ( by %s )", cup, team, cupPosition, user );
	}
}
