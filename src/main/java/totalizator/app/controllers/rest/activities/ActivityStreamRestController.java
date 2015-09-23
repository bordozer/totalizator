package totalizator.app.controllers.rest.activities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import totalizator.app.models.Match;
import totalizator.app.models.User;
import totalizator.app.models.activities.AbstractActivityStreamEntry;
import totalizator.app.models.activities.ActivityStreamEntryType;
import totalizator.app.models.activities.MatchActivityStreamEntry;
import totalizator.app.models.activities.MatchBetActivityStreamEntry;
import totalizator.app.models.activities.events.MatchBetEvent;
import totalizator.app.models.activities.events.MatchEvent;
import totalizator.app.services.DTOService;
import totalizator.app.services.UserService;
import totalizator.app.services.activiries.ActivityStreamService;
import totalizator.app.services.matches.MatchBetsService;
import totalizator.app.services.matches.MatchService;

import java.security.Principal;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@RestController
@RequestMapping( "/rest/activity-stream" )
public class ActivityStreamRestController {

	@Autowired
	private UserService userService;

	@Autowired
	private ActivityStreamService activityStreamService;

	@Autowired
	private MatchService matchService;

	@Autowired
	private MatchBetsService matchBetsService;

	@Autowired
	private DTOService dtoService;

	@RequestMapping( method = RequestMethod.GET, value = "/portal/" )
	public List<ActivityStreamDTO> portalPageActivities( final Principal principal ) {

		for ( final AbstractActivityStreamEntry entry : activityStreamService.loadAll() ) {
			activityStreamService.delete( entry.getId() );
		}

		return transformActivities( activityStreamService.loadAllForLast( 24 ), userService.findByLogin( principal.getName() ) );
	}

	@RequestMapping( method = RequestMethod.GET, value = "/matches/{matchId}/" )
	public List<ActivityStreamDTO> matchActivities( final @PathVariable( "matchId" ) int matchId, final Principal principal ) {
		return transformActivities( activityStreamService.loadAllForMatch( matchId ), userService.findByLogin( principal.getName() ) );
	}

	@RequestMapping( method = RequestMethod.GET, value = "/users/{userId}/" )
	public List<ActivityStreamDTO> userActivities( final @PathVariable( "userId" ) int userId, final Principal principal ) {
		return transformActivities( activityStreamService.loadAllForUser( userId ), userService.findByLogin( principal.getName() ) );
	}

	private List<ActivityStreamDTO> transformActivities( final List<AbstractActivityStreamEntry> entries, final User currentUser ) {

		return entries
				.stream()
				.filter( new Predicate<AbstractActivityStreamEntry>() {
					@Override
					public boolean test( final AbstractActivityStreamEntry entry ) {
						return entry.getActivityStreamEntryType() != ActivityStreamEntryType.MATCH_FINISHED;
					}
				} )
				.map( new Function<AbstractActivityStreamEntry, ActivityStreamDTO>() {

					@Override
					public ActivityStreamDTO apply( final AbstractActivityStreamEntry activity ) {

						final ActivityStreamDTO dto = new ActivityStreamDTO();

						dto.setActivityStreamEntryTypeId( activity.getActivityStreamEntryType().getId() );

						if ( activity.getActivityOfUser() != null ) {
							dto.setActivityOfUser( dtoService.transformUser( activity.getActivityOfUser() ) );
						}
						dto.setActivityTime( activity.getActivityTime() );

						initActivitySpecific( activity, currentUser, dto );

						return dto;
					}
				} )
				.collect( Collectors.toList() );
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

		dto.setMatch( dtoService.transformMatch( matchService.load( matchBetActivity.getActivityEntryId() ), currentUser ) );

		final Match match = matchService.load( matchBetActivity.getActivityEntryId() );
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
