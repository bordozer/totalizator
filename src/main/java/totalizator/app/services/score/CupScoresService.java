package totalizator.app.services.score;

import totalizator.app.beans.UserPoints;
import totalizator.app.models.Cup;
import totalizator.app.models.MatchBet;

import java.util.List;

public interface CupScoresService {

	int getUsersScores( final MatchBet matchBet );

	List<UserPoints> getUsersScores( final Cup cup );

	List<UserPoints> getUsersScoresSummary( Cup cup );
}
