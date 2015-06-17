package totalizator.app.services.score;

import org.apache.commons.collections15.CollectionUtils;
import org.apache.commons.collections15.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import totalizator.app.beans.UserPoints;
import totalizator.app.models.*;
import totalizator.app.services.*;

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

	@Autowired
	private CupService cupService;

	@Autowired
	private CupWinnerService cupWinnerService;

	@Autowired
	private CupBetsService cupBetsService;

	@Override
	@Cacheable( value = CACHE_QUERY )
	public int getUsersScores( final MatchBet matchBet ) {
		return ScoreCalculationStrategy.getInstance().getPoints( matchBet );
	}

	@Override
	@Cacheable( value = CACHE_QUERY )
	public List<UserPoints> getUsersScores( final Cup cup ) {

		final List<UserPoints> result = newArrayList();

		for ( final User user : userService.loadAll() ) {
			result.addAll( getUserPoints( cup, user ) );
		}

		/*CollectionUtils.filter( result, new Predicate<UserPoints>() {
			@Override
			public boolean evaluate( final UserPoints userPoints ) {
				return userPoints.getPoints() > 0;
			}
		} );*/

		return result;
	}

	@Override
	@Cacheable( value = CACHE_QUERY )
	public List<UserPoints> getUserPoints( final Cup cup, final User user ) {
		final List<UserPoints> userResult = newArrayList();

		final ScoreCalculationStrategy calculationStrategy = ScoreCalculationStrategy.getInstance();

		final List<MatchBet> userCupBets = matchBetsService.loadAll( cup, user );

		for ( final MatchBet bet : userCupBets ) {
			userResult.add( new UserPoints( user, calculationStrategy.getPoints( bet ) ) );
		}
		return userResult;
	}

	@Override
	@Cacheable( value = CACHE_QUERY )
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

		if ( cupService.isCupFinished( cup ) ) {
			for ( final CupTeamBet cupTeamBet : cupBetsService.load( cup ) ) {

				final Team team = cupTeamBet.getTeam();
				final User user = cupTeamBet.getUser();
				final int cupPosition = cupTeamBet.getCupPosition();

				final UserPoints userPoints = getUserPoints( result, user );

				if ( userPoints == null ) {
					result.add( new UserPoints( user, getUserCupWinnersPoints( cup, team, user, cupPosition ) ) );
					continue;
				}

				userPoints.setPoints( userPoints.getPoints() + getUserCupWinnersPoints( cup, team, user, cupPosition ) );
			}
		}

		Collections.sort( result, new Comparator<UserPoints>() {
			@Override
			public int compare( final UserPoints o1, final UserPoints o2 ) {
				return ( ( Integer ) o2.getPoints() ).compareTo( o1.getPoints() );
			}
		} );

		return result;
	}

	@Override
	@Cacheable( value = CACHE_QUERY )
	public int getUserCupWinnersPoints( final Cup cup, final Team team, final User user, final int cupPosition ) {

		if ( !cupService.isCupFinished( cup ) ) {
			return 0;
		}

		final List<CupWinner> winners = cupWinnerService.loadAll( cup );

		final int realTeamPositionInCupFinal = getTeamRealPosition( winners, team );

		if ( realTeamPositionInCupFinal == cupPosition ) {
			return 6;
		}

		final boolean userWinnerPresentsInWinners = CollectionUtils.find( winners, new Predicate<CupWinner>() {

			@Override
			public boolean evaluate( final CupWinner cupWinner ) {
				return cupWinner.getTeam().equals( team );
			}
		} ) != null;

		if ( userWinnerPresentsInWinners ) {
			return 3;
		}

		return 0;
	}

	private int getTeamRealPosition( final List<CupWinner> winners, final Team team ) {

		int position = 1;

		for ( final CupWinner winner : winners ) {

			if ( winner.getTeam().equals( team ) ) {
				return position;
			}

			position++;
		}

		return 0;
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
