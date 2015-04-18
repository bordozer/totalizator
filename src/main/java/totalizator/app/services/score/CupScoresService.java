package totalizator.app.services.score;

import totalizator.app.beans.UserPoints;
import totalizator.app.models.Cup;
import totalizator.app.models.MatchBet;
import totalizator.app.models.Team;
import totalizator.app.models.User;

import java.util.List;

public interface CupScoresService {

	int getUsersScores( final MatchBet matchBet );

	List<UserPoints> getUsersScores( final Cup cup );

	List<UserPoints> getUsersScoresSummary( Cup cup );

	int getUserCupWinnersPoints( Cup cup, final Team team, User user, final int cupPosition );
}
