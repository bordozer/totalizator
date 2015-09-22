package totalizator.app.controllers.rest.activities;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import totalizator.app.dto.BetDTO;
import totalizator.app.dto.MatchDTO;
import totalizator.app.dto.UserDTO;
import totalizator.app.dto.serialization.DateTimeSerializer;

import java.time.LocalDateTime;

public class ActivityStreamDTO {

	private int activityStreamEntryTypeId;
	private UserDTO activityOfUser;
	private BetDTO matchBet;
	private MatchDTO match;
	private LocalDateTime activityTime;

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

	public void setMatchBet( final BetDTO matchBet ) {
		this.matchBet = matchBet;
	}

	public BetDTO getMatchBet() {
		return matchBet;
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
}
