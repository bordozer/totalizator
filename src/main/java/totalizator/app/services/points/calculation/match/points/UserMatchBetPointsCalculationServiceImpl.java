package totalizator.app.services.points.calculation.match.points;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import totalizator.app.beans.points.UserMatchBetPointsHolder;
import totalizator.app.models.Cup;
import totalizator.app.models.MatchBet;
import totalizator.app.models.User;
import totalizator.app.services.matches.MatchBetsService;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static com.google.common.collect.Lists.newArrayList;

@Service
public class UserMatchBetPointsCalculationServiceImpl implements UserMatchBetPointsCalculationService {

	public static final Collector<UserMatchBetPointsHolder, ?, Integer> COLLECTOR = Collectors.summingInt( new ToIntFunction<UserMatchBetPointsHolder>() {
		@Override
		public int applyAsInt( final UserMatchBetPointsHolder value ) {
			return value.getMatchBetPoints();
		}
	} );

	@Autowired
	private MatchBetsService matchBetsService;

	@Override
	public UserMatchBetPointsHolder getUserMatchBetPoints( final MatchBet matchBet ) {
		return new UserMatchBetPointsHolder( matchBet, getPointsCalculationStrategy( matchBet.getMatch().getCup() ).getPoints( matchBet ) );
	}

	@Override
	public List<UserMatchBetPointsHolder> getUsersMatchBetsPointHolders( final Cup cup, final List<User> users ) {

		final List<UserMatchBetPointsHolder> result = newArrayList();

		for ( final User user : users ) {
			result.addAll( getUserPoints( cup, user ) );
		}

		return result;
	}

	@Override
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

	@Override
	public int getUserMatchBetPointsNegative( final Cup cup, final User user ) {

		return getUserPoints( cup, user )
				.stream()
				.filter( new Predicate<UserMatchBetPointsHolder>() {
					@Override
					public boolean test( final UserMatchBetPointsHolder userMatchBetPointsHolder ) {
						return userMatchBetPointsHolder.getPoints() < 0;
					}
				} ).collect( COLLECTOR );
	}

	@Override
	public int getUserMatchBetPointsPositive( final Cup cup, final User user ) {

		return getUserPoints( cup, user )
				.stream()
				.filter( new Predicate<UserMatchBetPointsHolder>() {
					@Override
					public boolean test( final UserMatchBetPointsHolder userMatchBetPointsHolder ) {
						return userMatchBetPointsHolder.getPoints() > 0;
					}
				} ).collect( COLLECTOR );
	}

	private BetPointsCalculationStrategy getPointsCalculationStrategy( final Cup cup ) {
		return BetPointsCalculationStrategy.getInstance( cup.getPointsCalculationStrategy() );
	}

	public void setMatchBetsService( final MatchBetsService matchBetsService ) {
		this.matchBetsService = matchBetsService;
	}
}
