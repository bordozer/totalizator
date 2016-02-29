package totalizator.app.controllers.rest.activities;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import totalizator.app.beans.points.UserMatchBetPointsHolder;
import totalizator.app.beans.points.UserMatchPointsHolder;
import totalizator.app.controllers.rest.portal.UsersRatingPositionDTO;
import totalizator.app.dto.MatchDTO;
import totalizator.app.dto.UserDTO;
import totalizator.app.dto.serialization.DateTimeSerializer;

import java.time.LocalDateTime;

public class ActivityStreamDTO {

	private int activityStreamEntryTypeId;
	private UserDTO activityOfUser;
	private LocalDateTime activityTime;

	private MatchDTO match;

	private int score1;
	private int score2;

	private int oldScore1;
	private int oldScore2;

	private boolean showBetData;
	private boolean showOldScores;
	private UsersRatingPositionDTO activityBetPoints;

	public void setActivityStreamEntryTypeId( final int activityStreamEntryTypeId ) {
		this.activityStreamEntryTypeId = activityStreamEntryTypeId;
	}

	public int getActivityStreamEntryTypeId() {
		return activityStreamEntryTypeId;
	}

	public UserDTO getActivityOfUser() {
		return activityOfUser;
	}

	public void setActivityOfUser( final UserDTO activityOfUser ) {
		this.activityOfUser = activityOfUser;
	}

	public void setMatch( final MatchDTO match ) {
		this.match = match;
	}

	public MatchDTO getMatch() {
		return match;
	}

	public void setActivityTime( final LocalDateTime activityTime ) {
		this.activityTime = activityTime;
	}

	@JsonSerialize( using = DateTimeSerializer.class )
	public LocalDateTime getActivityTime() {
		return activityTime;
	}

	public int getScore1() {
		return score1;
	}

	public void setScore1( final int score1 ) {
		this.score1 = score1;
	}

	public int getScore2() {
		return score2;
	}

	public void setScore2( final int score2 ) {
		this.score2 = score2;
	}

	public int getOldScore1() {
		return oldScore1;
	}

	public void setOldScore1( final int oldScore1 ) {
		this.oldScore1 = oldScore1;
	}

	public int getOldScore2() {
		return oldScore2;
	}

	public void setOldScore2( final int oldScore2 ) {
		this.oldScore2 = oldScore2;
	}

	public boolean isShowBetData() {
		return showBetData;
	}

	public void setShowBetData( final boolean showBetData ) {
		this.showBetData = showBetData;
	}

	public void setShowOldScores( final boolean showOldScores ) {
		this.showOldScores = showOldScores;
	}

	public boolean isShowOldScores() {
		return showOldScores;
	}

	public UsersRatingPositionDTO getActivityBetPoints() {
		return activityBetPoints;
	}

	public void setActivityBetPoints(final UsersRatingPositionDTO activityBetPoints) {
		this.activityBetPoints = activityBetPoints;
	}
}
