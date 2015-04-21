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
import totalizator.app.translator.Language;
import totalizator.app.translator.TranslatorService;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

@Service
public class UserTitleServiceImpl implements UserTitleService {

	@Autowired
	private CupService cupService;

	@Autowired
	private MatchService matchService;

	@Autowired
	private MatchBetsService matchBetsService;

	@Autowired
	private CupScoresService cupScoresService;

	@Autowired
	private TranslatorService translatorService;

	@Override
	public UserTitle getUserTitle( final User user ) {

		final List<MatchBet> bets = matchBetsService.loadAll( user );
		CollectionUtils.filter( bets, new Predicate<MatchBet>() {

			@Override
			public boolean evaluate( final MatchBet matchBet ) {
				return matchService.isMatchFinished( matchBet.getMatch() );
			}
		} );

		final int betsCount = bets.size();

		final List<UserPoints> userPoints = newArrayList();
		for ( final Cup cup : cupService.loadAllCurrent() ) {
			userPoints.addAll( cupScoresService.getUserPoints( cup, user ) );
		}

		int points = 0;
		for ( final UserPoints userPoint : userPoints ) {
			points += userPoint.getPoints();
		}

		final int luckyLevel = points * 100 / betsCount;

		if ( luckyLevel > 0 && luckyLevel <= 30 ) {
			return new UserTitle( translatorService.translate( "User title: 1", getLanguage() ), "fa fa-meh-o" );
		}

		if ( luckyLevel > 31 && luckyLevel <= 50 ) {
			return new UserTitle( translatorService.translate( "User title: 2", getLanguage() ), "fa fa-smile-o" );
		}

		if ( luckyLevel > 51 && luckyLevel <= 80 ) {
			return new UserTitle( translatorService.translate( "User title: 3", getLanguage() ), "fa fa-fire" );
		}

		if ( luckyLevel > 81 && luckyLevel <= 90 ) {
			return new UserTitle( translatorService.translate( "User title: 4", getLanguage() ), "fa fa-sun-o" );
		}

		if ( luckyLevel > 91 ) {
			return new UserTitle( translatorService.translate( "User title: 5", getLanguage() ), "fa fa-diamond" );
		}

		return new UserTitle( translatorService.translate( "User title: 0", getLanguage() ), "fa fa-frown-o" );
	}

	private Language getLanguage() {
		return Language.RU; // TODO: language!!!
	}
}
