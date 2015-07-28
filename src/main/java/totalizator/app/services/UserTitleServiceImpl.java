package totalizator.app.services;

import org.apache.commons.collections15.CollectionUtils;
import org.apache.commons.collections15.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import totalizator.app.beans.UserPoints;
import totalizator.app.beans.UserTitle;
import totalizator.app.models.Cup;
import totalizator.app.models.MatchBet;
import totalizator.app.models.User;
import totalizator.app.services.score.CupScoresService;
import totalizator.app.translator.TranslatorService;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

@Service
public class UserTitleServiceImpl implements UserTitleService {

	@Autowired
	private MatchService matchService;

	@Autowired
	private MatchBetsService matchBetsService;

	@Autowired
	private CupScoresService cupScoresService;

	@Autowired
	private TranslatorService translatorService;

	private final static List<String> ICONS = newArrayList( "fa fa-ban", "fa fa-frown-o", "fa fa-meh-o", "fa fa-smile-o", "fa fa-graduation-cap", "fa fa-magic" );

	@Override
	public UserTitle getUserTitle( final User user, final Cup cup ) {

		final List<MatchBet> bets = matchBetsService.loadAll( cup, user );
		CollectionUtils.filter( bets, new Predicate<MatchBet>() {

			@Override
			public boolean evaluate( final MatchBet matchBet ) {
				return matchService.isMatchFinished( matchBet.getMatch() );
			}
		} );

		final int betsCount = bets.size();

		final List<UserPoints> userPoints = cupScoresService.getUserPoints( cup, user );

		int points = 0;
		for ( final UserPoints userPoint : userPoints ) {
			points += userPoint.getPoints();
		}

		if ( betsCount == 0 ) {
			return new UserTitle( getTitle( "User title: -1", 0 ), "fa fa-close" );
		}

		final int luckyLevel = points * 100 / betsCount;

		if ( luckyLevel > 0 && luckyLevel <= 30 ) {
			return new UserTitle( getTitle( "User title: 1", luckyLevel ), ICONS.get( 1 ) );
		}

		if ( luckyLevel >= 31 && luckyLevel <= 50 ) {
			return new UserTitle( getTitle( "User title: 2", luckyLevel ), ICONS.get( 2 ) );
		}

		if ( luckyLevel >= 51 && luckyLevel <= 80 ) {
			return new UserTitle( getTitle( "User title: 3", luckyLevel ), ICONS.get( 3 ) );
		}

		if ( luckyLevel >= 81 && luckyLevel <= 90 ) {
			return new UserTitle( getTitle( "User title: 4", luckyLevel ), ICONS.get( 4 ) );
		}

		if ( luckyLevel >= 91 ) {
			return new UserTitle( getTitle( "User title: 5", luckyLevel ), ICONS.get( 5 ) );
		}

		return new UserTitle( getTitle( "User title: 0", luckyLevel ), ICONS.get( 0 ) );
	}

	private String getTitle( final String nerd, final int luckyLevel ) {
		return String.format( "%s %s", translatorService.translate( nerd, translatorService.getDefaultLanguage() ), luckyLevel ) + "%";
	}
}
