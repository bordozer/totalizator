package totalizator.app.services.score;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import totalizator.app.beans.points.UserMatchBetPointsHolder;
import totalizator.app.models.Cup;
import totalizator.app.models.MatchBet;
import totalizator.app.models.User;
import totalizator.app.services.matches.MatchBetsService;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.google.common.collect.Lists.newArrayList;

@Service
public class UserMatchBetPointsCalculationServiceImpl implements UserMatchBetPointsCalculationService {

	@Autowired
	private MatchBetsService matchBetsService;

	@Override
	@Cacheable( value = CACHE_QUERY )
	public UserMatchBetPointsHolder getUserMatchBetPoints( final MatchBet matchBet ) {
		return new UserMatchBetPointsHolder( matchBet, getPointsCalculationStrategy( matchBet.getMatch().getCup() ).getPoints( matchBet ) );
	}

	@Override
	@Cacheable( value = CACHE_QUERY )
	public List<UserMatchBetPointsHolder> getUsersMatchBetsPointHolders( final Cup cup, final List<User> users ) {

		final List<UserMatchBetPointsHolder> result = newArrayList();

		for ( final User user : users ) {
			result.addAll( getUserPoints( cup, user ) );
		}

		return result;
	}

	@Override
	@Cacheable( value = CACHE_QUERY )
	public List<UserMatchBetPointsHolder> getUserPoints( final Cup cup, final User user ) {

		return matchBetsService.loadAll( cup, user )
				.stream()
				.map( new Function<MatchBet, UserMatchBetPointsHolder>() {
					@Override
					public UserMatchBetPointsHolder apply( final MatchBet matchBet ) {
						return getUserMatchBetPoints( matchBet );
					}
				} )
				.collect( Collectors.toList() );
	}

	private BetPointsCalculationStrategy getPointsCalculationStrategy( final Cup cup ) {
		return BetPointsCalculationStrategy.getInstance( cup.getPointsCalculationStrategy() );
	}

	public void setMatchBetsService( final MatchBetsService matchBetsService ) {
		this.matchBetsService = matchBetsService;
	}
}
