package totalizator.app.services.score;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import totalizator.app.beans.points.UserMatchBetPointsHolder;
import totalizator.app.models.Match;
import totalizator.app.models.MatchBet;
import totalizator.app.models.User;
import totalizator.app.services.matches.MatchBetsService;

import java.util.List;

@Service
public class MatchBonusPointsCalculationServiceImpl implements MatchBonusPointsCalculationService {

	@Autowired
	private MatchBetsService matchBetsService;

	@Autowired UserMatchBetPointsCalculationService userMatchBetPointsCalculationService;

	@Override
	@Cacheable( value = CACHE_QUERY )
	public float calculateMatchBonus( final Match match, final List<User> users ) {

		final List<MatchBet> matchBets = matchBetsService.loadAll( match );

		int winnersCount = 0;
		int losersPointsSum = 0;

		for ( final MatchBet bet : matchBets ) {

			if ( ! users.contains( bet.getUser() ) ) {
				continue;
			}

			final UserMatchBetPointsHolder userBetPoints = userMatchBetPointsCalculationService.getUserMatchBetPoints( bet );

			if ( userBetPoints.getPoints() > 0 ) {
				winnersCount++;
			}

			if ( userBetPoints.getPoints() < 0 ) {
				losersPointsSum += Math.abs( userBetPoints.getPoints() );
			}
		}

		if ( winnersCount == 0 ) {
			return 0; // nobody won
		}

		return losersPointsSum / winnersCount;
	}
}
