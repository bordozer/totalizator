package totalizator.app.services.activiries;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import totalizator.app.dao.ActivityStreamDao;
import totalizator.app.models.ActivityStreamEntry;
import totalizator.app.models.MatchBet;
import totalizator.app.models.User;
import totalizator.app.models.activities.AbstractActivityStreamEntry;
import totalizator.app.models.activities.ActivityStreamEntryType;
import totalizator.app.models.activities.MatchActivityStreamEntry;
import totalizator.app.models.activities.MatchBetActivityStreamEntry;
import totalizator.app.models.activities.events.MatchBetEvent;
import totalizator.app.models.activities.events.MatchEvent;
import totalizator.app.services.matches.MatchBetsService;
import totalizator.app.services.matches.MatchService;
import totalizator.app.services.utils.DateTimeService;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ActivityStreamServiceImpl implements ActivityStreamService {

	@Autowired
	private ActivityStreamDao activityStreamRepository;

	@Autowired
	private DateTimeService dateTimeService;

	@Autowired
	private MatchBetsService matchBetsService;

	@Autowired
	private MatchService matchService;

	@Override
	public List<AbstractActivityStreamEntry> loadAll() {

		return activityStreamRepository.loadAll() // TODO: limit by page
				.stream()
				.map( new Function<ActivityStreamEntry, AbstractActivityStreamEntry>() {

					@Override
					public AbstractActivityStreamEntry apply( final ActivityStreamEntry activityStreamEntry ) {

						switch ( activityStreamEntry.getActivityStreamEntryType() ) {
							case MATCH_BET_CREATED:
							case MATCH_BET_CHANGED:
								return new MatchBetActivityStreamEntry( activityStreamEntry, matchBetsService.load( activityStreamEntry.getActivityEntryId() ) );
							case MATCH_BET_DELETED:
								return new MatchActivityStreamEntry( activityStreamEntry, matchService.load( activityStreamEntry.getActivityEntryId() ) );
						}

						throw new IllegalArgumentException( String.format( "Unsupported activity type: %s", activityStreamEntry.getActivityStreamEntryType() ) );
					}
				} )
				.collect( Collectors.toList() );
	}

	@Override
	@Transactional
	public void matchBetCreated( final MatchBet matchBet ) {
		activityStreamRepository.save( createFromMatchBet( matchBet, ActivityStreamEntryType.MATCH_BET_CREATED ) );
	}

	@Override
	@Transactional
	public void matchBetChanged( final MatchBet matchBet ) {
		activityStreamRepository.save( createFromMatchBet( matchBet, ActivityStreamEntryType.MATCH_BET_CHANGED ) );
	}

	@Override
	public void matchBetDeleted( final User user, final int matchId ) {

		final ActivityStreamEntry activity = new ActivityStreamEntry( user, dateTimeService.getNow() );

		activity.setActivityEntryId( matchId );
		activity.setActivityStreamEntryType( ActivityStreamEntryType.MATCH_BET_DELETED );
		activity.setEvent( new MatchEvent( matchId ) );

		activityStreamRepository.save( activity );
	}

	private ActivityStreamEntry createFromMatchBet( final MatchBet matchBet, final ActivityStreamEntryType activityStreamEntryType ) {

		final ActivityStreamEntry activity = new ActivityStreamEntry( matchBet.getUser(), matchBet.getBetTime() );

		activity.setActivityEntryId( matchBet.getId() );
		activity.setActivityStreamEntryType( activityStreamEntryType );
		activity.setEvent( new MatchBetEvent( matchBet.getId(), matchBet.getBetScore1(), matchBet.getBetScore2() ) );

		return activity;
	}
}
