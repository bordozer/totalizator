package totalizator.app.services;

import totalizator.app.beans.ValidationResult;
import totalizator.app.models.Cup;
import totalizator.app.models.Match;
import totalizator.app.models.MatchBet;
import totalizator.app.models.User;

import java.util.List;

public interface MatchBetsService extends GenericService<MatchBet>{

	List<MatchBet> loadAll( final User user );

	List<MatchBet> loadAll( final Match match );

	List<MatchBet> loadAll( final Cup cup, final User user );

	MatchBet load( final User user, final Match match );

	MatchBet load( final int userId, final int matchId );

	int betsCount( final Match match );

	ValidationResult validateBettingAllowed( final Match match, final User user );

	boolean canMatchBeBet( final Match match, final User user );

	boolean userCanSeeAnotherBets( final Match match, final User user );

	boolean isAllowedToShowMatchBets( final MatchBet matchBet, final User user );
}
