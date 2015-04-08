package totalizator.app.models;

import totalizator.app.enums.CupPosition;

import javax.persistence.*;

import java.util.Date;

import static totalizator.app.models.TeamBet.*;

@Entity
@Table(
		name = "teamBets"
)
@NamedQueries( {
		@NamedQuery(
				name = LOAD_ALL,
				query = "select t from TeamBet t order by betTime desc"
		),
		@NamedQuery(
				name = LOAD_ALL_FOR_CUP,
				query = "select t from TeamBet t where cupId= :cupId order by betTime desc"
		),
		@NamedQuery(
				name = LOAD_ALL_FOR_USER,
				query = "select t from TeamBet t where userId= :userId order by betTime desc"
		),
		@NamedQuery(
				name = LOAD_ALL_FOR_CUP_AND_USER,
				query = "select t from TeamBet t where cupId= :cupId and userId= :userId order by betTime desc"
		)
} )
public class TeamBet extends AbstractEntity {

	public static final String LOAD_ALL = "teamBet.loadAll";
	public static final String LOAD_ALL_FOR_CUP = "teamBet.loadForCup";
	public static final String LOAD_ALL_FOR_USER = "teamBet.loadForUser";
	public static final String LOAD_ALL_FOR_CUP_AND_USER = "teamBet.loadForCupAndUser";

	@ManyToOne
	@JoinColumn(name="cupId")
	private Cup cup;

	@ManyToOne
	@JoinColumn(name="userId")
	private User user;

	private CupPosition cupPosition;

	private Date betTime;

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

	public CupPosition getCupPosition() {
		return cupPosition;
	}

	public void setCupPosition( final CupPosition cupPosition ) {
		this.cupPosition = cupPosition;
	}

	public Date getBetTime() {
		return betTime;
	}

	public void setBetTime( final Date betTime ) {
		this.betTime = betTime;
	}

	@Override
	public String toString() {
		return String.format( "%s: %s %s", cup, user, cupPosition );
	}
}
