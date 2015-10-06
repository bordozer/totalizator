package totalizator.app.services.activiries;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import totalizator.app.controllers.rest.activities.ActivityStreamDTO;
import totalizator.app.models.Match;
import totalizator.app.models.User;
import totalizator.app.models.activities.AbstractActivityStreamEntry;
import totalizator.app.models.activities.ActivityStreamEntryType;
import totalizator.app.models.activities.MatchActivityStreamEntry;
import totalizator.app.models.activities.MatchBetActivityStreamEntry;
import totalizator.app.models.activities.events.MatchBetEvent;
import totalizator.app.models.activities.events.MatchEvent;
import totalizator.app.services.DTOService;
import totalizator.app.services.matches.MatchBetsService;
import totalizator.app.services.matches.MatchService;

@Service
public class ActivityDTOServiceImpl implements ActivityDTOService {

	@Autowired
	private MatchService matchService;

	@Autowired
	private MatchBetsService matchBetsService;

	@Autowired
	private DTOService dtoService;

	@Autowired
	private ActivityStreamValidator activityStreamValidator;

	@Override
	@Cacheable( value = CACHE_ACTIVITY )
	public ActivityStreamDTO transformActivity( final AbstractActivityStreamEntry activity, final User currentUser ) {

		if ( ! activityStreamValidator.validate( activity ) ) {
			return null;
		}

		final ActivityStreamDTO dto = new ActivityStreamDTO();

		dto.setActivityStreamEntryTypeId( activity.getActivityStreamEntryType().getId() );

		if ( activity.getActivityOfUser() != null ) {
			dto.setActivityOfUser( dtoService.transformUser( activity.getActivityOfUser() ) );
		}
		dto.setActivityTime( activity.getActivityTime() );

		initActivitySpecific( activity, currentUser, dto );

		return dto;
	}

	private void initActivitySpecific( final AbstractActivityStreamEntry activity, final User currentUser, final ActivityStreamDTO dto ) {

		switch ( activity.getActivityStreamEntryType() ) {
			case MATCH_BET_CREATED:
			case MATCH_BET_CHANGED:
				initMatchBet( activity, currentUser, dto );
				return;
			case MATCH_BET_DELETED:
			case MATCH_FINISHED:
				initMatch( activity, currentUser, dto );
				return;
		}
	}

	private void initMatch( final AbstractActivityStreamEntry activity, final User currentUser, final ActivityStreamDTO dto ) {

		final MatchActivityStreamEntry matchBetActivity = ( MatchActivityStreamEntry ) activity;

		final Match match = matchService.load( matchBetActivity.getActivityEntryId() );

		if ( match == null ) {
			return;
		}

		dto.setMatch( dtoService.transformMatch( match, currentUser ) );

		final boolean showBetData = matchBetActivity.getActivityStreamEntryType() != ActivityStreamEntryType.MATCH_FINISHED && showBetData( currentUser, matchBetActivity, match );
		dto.setShowBetData( showBetData );

		if ( showBetData ) {

			final MatchEvent event = matchBetActivity.getMatchEvent();

			dto.setScore1( event.getScore1() );
			dto.setScore2( event.getScore2() );

			dto.setShowOldScores( false );
		}
	}

	private void initMatchBet( final AbstractActivityStreamEntry activity, final User currentUser, final ActivityStreamDTO dto ) {

		final MatchBetActivityStreamEntry matchBetActivity = ( MatchBetActivityStreamEntry ) activity;

		final Match match = matchService.load( matchBetActivity.getActivityEntryId() );
		dto.setMatch( dtoService.transformMatch( match, currentUser ) );

		final boolean showBetData = showBetData( currentUser, matchBetActivity, match );
		dto.setShowBetData( showBetData );

		if ( showBetData ) {

			final MatchBetEvent event = matchBetActivity.getMatchBetEvent();

			dto.setScore1( event.getScore1() );
			dto.setScore2( event.getScore2() );

			dto.setOldScore1( event.getOldScore1() );
			dto.setOldScore2( event.getOldScore2() );

			dto.setShowOldScores( matchBetActivity.getActivityStreamEntryType() == ActivityStreamEntryType.MATCH_BET_CHANGED );
		}
	}

	private boolean showBetData( final User currentUser, final AbstractActivityStreamEntry matchBetActivity, final Match match ) {
		return matchBetActivity.getActivityOfUser().equals( currentUser ) || matchBetsService.userCanSeeAnotherBets( match, currentUser );
	}

}
