package totalizator.app.services.score;

import totalizator.app.models.Cup;
import totalizator.app.beans.UserPoints;

import java.util.List;

public interface CupScoresService {

	List<UserPoints> getUsersScores( final Cup cup );
}
