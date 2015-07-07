package totalizator.app.services.score;

import totalizator.app.beans.UserPoints;
import totalizator.app.models.*;

import java.util.List;

public interface CupScoresService {

	String CACHE_QUERY = "totalizator.app.cache.cup-scores.query";

	int getUsersScores( final MatchBet matchBet );

	List<UserPoints> getUsersScores( final Cup cup );

	List<UserPoints> getUsersScores( final Cup cup, final UserGroup userGroup );

	List<UserPoints> getUserPoints( final Cup cup, final User user );

	List<UserPoints> getUsersScoresSummary( final Cup cup );

	List<UserPoints> getUsersScoresSummary( final Cup cup, final UserGroup userGroup );

	int getUserCupWinnersPoints( final Cup cup, final Team team, User user, final int cupPosition );
}
