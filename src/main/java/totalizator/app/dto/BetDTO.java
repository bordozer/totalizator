package totalizator.app.dto;

public class BetDTO {

	private int matchBetId;

	private MatchDTO match;
	private UserDTO user;

	private int score1;
	private int score2;

	private boolean securedBet;

	public BetDTO() {
	}

	public BetDTO( final MatchDTO match, final UserDTO user ) {
		this.match = match;
		this.user = user;
	}

	public int getMatchBetId() {
		return matchBetId;
	}

	public void setMatchBetId( final int matchBetId ) {
		this.matchBetId = matchBetId;
	}

	public MatchDTO getMatch() {
		return match;
	}

	public void setMatch( final MatchDTO match ) {
		this.match = match;
	}

	public UserDTO getUser() {
		return user;
	}

	public void setUser( final UserDTO user ) {
		this.user = user;
	}

	public int getScore1() {
		return score1;
	}

	public void setScore1( final int score1 ) {
		this.score1 = score1;
	}

	public int getScore2() {
		return score2;
	}

	public void setScore2( final int score2 ) {
		this.score2 = score2;
	}

	public boolean isSecuredBet() {
		return securedBet;
	}

	public void setSecuredBet( boolean securedBet ) {
		this.securedBet = securedBet;
	}

	@Override
	public String toString() {
		return String.format( "%s %s %d %d", match, user, score1, score1 );
	}
}
