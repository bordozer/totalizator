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

	@Override
	public List<AbstractActivityStreamEntry> loadAll() {
		return transformEntries( activityStreamRepository.loadAll() );
	}

	@Override
	public List<AbstractActivityStreamEntry> loadAllForLast( final int hours ) {
		return transformEntries( activityStreamRepository.loadAllEarlierThen( dateTimeService.minusHours( hours ) ) );
	}

	@Override
	public List<AbstractActivityStreamEntry> loadAllForMatch( final int matchId ) {
		return transformEntries( activityStreamRepository.loadAllForMatch( matchId ) );
	}

	@Override
	public List<AbstractActivityStreamEntry> loadAllForUser( final int userId ) {
		return transformEntries( activityStreamRepository.loadAllForUser( userId ) );
	}

	@Override
	@Transactional
	public void matchBetCreated( final MatchBet matchBet ) {
		activityStreamRepository.save( createFromMatchBet( matchBet, ActivityStreamEntryType.MATCH_BET_CREATED, 0, 0 ) );
	}

	@Override
	@Transactional
	public void matchBetChanged( final MatchBet matchBet, final int oldScore1, final int oldScore2 ) {
		activityStreamRepository.save( createFromMatchBet( matchBet, ActivityStreamEntryType.MATCH_BET_CHANGED, oldScore1, oldScore2 ) );
	}

	@Override
	public void matchBetDeleted( final User user, final int matchId, final int score1, final int score2 ) {
		activityStreamRepository.save( getMatchBetEvent( user, matchId, score1, score2, ActivityStreamEntryType.MATCH_BET_DELETED ) );
	}

	@Override
	public void matchFinished( final int matchId, final int score1, final int score2 ) {
		activityStreamRepository.save( getMatchBetEvent( null, matchId, score1, score2, ActivityStreamEntryType.MATCH_FINISHED ) );
	}

	@Override
	@Transactional
	public void delete( final int id ) {
		activityStreamRepository.delete( id );
	}

	private List<AbstractActivityStreamEntry> transformEntries( List<ActivityStreamEntry> activityStreamEntries ) {

		return activityStreamEntries
				.stream()
				.map( new Function<ActivityStreamEntry, AbstractActivityStreamEntry>() {

					@Override
					public AbstractActivityStreamEntry apply( final ActivityStreamEntry activityStreamEntry ) {

						switch ( activityStreamEntry.getActivityStreamEntryType() ) {
							case MATCH_BET_CREATED:
							case MATCH_BET_CHANGED:
								return new MatchBetActivityStreamEntry( activityStreamEntry );
							case MATCH_BET_DELETED:
							case MATCH_FINISHED:
								return new MatchActivityStreamEntry( activityStreamEntry );
						}

						throw new IllegalArgumentException( String.format( "Unsupported activity type: %s", activityStreamEntry.getActivityStreamEntryType() ) );
					}
				} )
				.collect( Collectors.toList() );
	}

	private ActivityStreamEntry getMatchBetEvent( final User user, final int matchId, final int score1, final int score2, ActivityStreamEntryType activityStreamEntryType ) {

		final ActivityStreamEntry activity = new ActivityStreamEntry( user, dateTimeService.getNow() );

		activity.setActivityEntryId( matchId );
		activity.setActivityStreamEntryType( activityStreamEntryType );
		activity.setEvent( new MatchEvent( matchId, score1, score2 ) );
		return activity;
	}

	private ActivityStreamEntry createFromMatchBet( final MatchBet matchBet, final ActivityStreamEntryType activityStreamEntryType, final int oldScore1, final int oldScore2 ) {

		final ActivityStreamEntry activity = new ActivityStreamEntry( matchBet.getUser(), matchBet.getBetTime() );

		final int matchId = matchBet.getMatch().getId();
		activity.setActivityEntryId( matchId );
		activity.setActivityStreamEntryType( activityStreamEntryType );
		activity.setEvent( new MatchBetEvent( matchId, matchBet.getBetScore1(), matchBet.getBetScore2(), oldScore1, oldScore2 ) );

		return activity;
	}
}
