package totalizator.app.services.points.calculation.cup;

import org.apache.commons.collections15.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import totalizator.app.models.*;
import totalizator.app.services.CupBetsService;
import totalizator.app.services.CupService;
import totalizator.app.services.CupWinnerService;

import java.util.List;
import java.util.Map;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

@Service
public class UserCupWinnersBonusCalculationServiceImpl implements UserCupWinnersBonusCalculationService {

	@Autowired
	private CupService cupService;

	@Autowired
	private CupBetsService cupBetsService;

	@Autowired
	private CupWinnerService cupWinnerService;

	@Override
	public int getUserCupWinnersSummaryPoints( final Cup cup, final User user ) {

		final Map<User, Integer> map = cupBetsService.load( cup, user )
				.stream()
				.collect(
						Collectors.groupingBy( CupTeamBet::getUser, Collectors.summingInt( new ToIntFunction<CupTeamBet>() {
							@Override
							public int applyAsInt( final CupTeamBet cupTeamBet ) {
								return getUserCupWinnerPoints( cup, cupTeamBet.getTeam(), user, cupTeamBet.getCupPosition() );
							}
						} ) )
				);

		if ( map != null && map.get( user ) != null ) {
			return map.get( user );
		}

		return 0;
	}

	@Override
	public int getUserCupWinnerPoints( final Cup cup, final Team team, final User user, final int cupPosition ) {

		if ( !cupService.isCupFinished( cup ) ) {
			return 0;
		}

		final List<CupWinner> winners = cupWinnerService.loadAll( cup );

		final int realTeamPositionInCupFinal = getTeamRealPosition( winners, team );

		if ( realTeamPositionInCupFinal == cupPosition ) {
			return 6;
		}

		final boolean userWinnerPresentsInWinners = CollectionUtils.find( winners, cupWinner -> cupWinner.getTeam().equals( team ) ) != null;

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

	public void setCupWinnerService( final CupWinnerService cupWinnerService ) {
		this.cupWinnerService = cupWinnerService;
	}

	public void setCupService( final CupService cupService ) {
		this.cupService = cupService;
	}
}
