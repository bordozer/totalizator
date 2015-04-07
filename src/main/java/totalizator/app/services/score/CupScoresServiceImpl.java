package totalizator.app.services.score;

import org.apache.commons.collections15.CollectionUtils;
import org.apache.commons.collections15.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import totalizator.app.beans.UserPoints;
import totalizator.app.models.Cup;
import totalizator.app.models.Match;
import totalizator.app.models.MatchBet;
import totalizator.app.models.User;
import totalizator.app.services.MatchBetsService;
import totalizator.app.services.UserService;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

@Service
public class CupScoresServiceImpl implements CupScoresService {

	@Autowired
	private UserService userService;

	@Autowired
	private MatchBetsService matchBetsService;

	@Override
	public int getUsersScores( final MatchBet matchBet ) {
		return ScoreCalculationStrategy.getInstance().getPoints(  matchBet  );
	}

	@Override
	public List<UserPoints> getUsersScores( final Cup cup ) {

		final ScoreCalculationStrategy calculationStrategy = ScoreCalculationStrategy.getInstance();

		final List<UserPoints> result = newArrayList();

		for ( final User user : userService.loadAll() ) {

			final List<MatchBet> userCupBets = matchBetsService.loadAll( cup, user );

			for ( final MatchBet bet : userCupBets ) {
				result.add( new UserPoints( user, calculationStrategy.getPoints( bet ) ) );
			}
		}

		CollectionUtils.filter( result, new Predicate<UserPoints>() {
			@Override
			public boolean evaluate( final UserPoints userPoints ) {
				return userPoints.getPoints() > 0;
			}
		} );

		return result;
	}

	@Override
	public List<UserPoints> getUsersScoresSummary( final Cup cup ) {
		final List<UserPoints> usersScores = getUsersScores( cup );

		final List<UserPoints> result = newArrayList();
		for ( final UserPoints usersScore : usersScores ) {

			final UserPoints userPoints = getUserPoints( result, usersScore.getUser() );

			if ( userPoints == null ) {
				result.add( new UserPoints( usersScore.getUser(), usersScore.getPoints() ) );
				continue;
			}

			userPoints.setPoints( userPoints.getPoints() + usersScore.getPoints() );
		}

		Collections.sort( result, new Comparator<UserPoints>() {
			@Override
			public int compare( final UserPoints o1, final UserPoints o2 ) {
				return ( ( Integer ) o2.getPoints() ).compareTo( o1.getPoints() );
			}
		} );

		return result;
	}

	private UserPoints getUserPoints( List<UserPoints> usersScores, final User user ) {

		for ( final UserPoints usersScore : usersScores ) {
			if ( usersScore.getUser().equals( user ) ) {
				return usersScore;
			}
		}

		return null;
	}
}
