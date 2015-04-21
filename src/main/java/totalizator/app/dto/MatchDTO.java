package totalizator.app.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import totalizator.app.dto.serialization.DateTimeDeserializer;
import totalizator.app.dto.serialization.DateTimeSerializer;

import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MatchDTO {

	private int matchId;

	private CategoryDTO category;

	private CupDTO cup;

	private TeamDTO team1;
	private int score1;

	private TeamDTO team2;
	private int score2;

	private LocalDateTime beginningTime;

	private boolean matchStarted;
	private boolean matchFinished;

	private boolean showAnotherBets;

	private int homeTeamNumber;

	public int getMatchId() {
		return matchId;
	}

	public void setMatchId( final int matchId ) {
		this.matchId = matchId;
	}

	public CategoryDTO getCategory() {
		return category;
	}

	public void setCategory( final CategoryDTO category ) {
		this.category = category;
	}

	public CupDTO getCup() {
		return cup;
	}

	public void setCup( final CupDTO cup ) {
		this.cup = cup;
	}

	public TeamDTO getTeam1() {
		return team1;
	}

	public void setTeam1( final TeamDTO team1 ) {
		this.team1 = team1;
	}

	public int getScore1() {
		return score1;
	}

	public void setScore1( final int score1 ) {
		this.score1 = score1;
	}

	public TeamDTO getTeam2() {
		return team2;
	}

	public void setTeam2( final TeamDTO team2 ) {
		this.team2 = team2;
	}

	public int getScore2() {
		return score2;
	}

	public void setScore2( final int score2 ) {
		this.score2 = score2;
	}

	@JsonSerialize(using = DateTimeSerializer.class)
	public LocalDateTime getBeginningTime() {
		return beginningTime;
	}

	@JsonDeserialize(using = DateTimeDeserializer.class)
	public void setBeginningTime( final LocalDateTime beginningTime ) {
		this.beginningTime = beginningTime;
	}

	public boolean isMatchFinished() {
		return matchFinished;
	}

	public void setMatchFinished( final boolean matchFinished ) {
		this.matchFinished = matchFinished;
	}

	public boolean isMatchStarted() {
		return matchStarted;
	}

	public void setMatchStarted( final boolean matchStarted ) {
		this.matchStarted = matchStarted;
	}

	public boolean isShowAnotherBets() {
		return showAnotherBets;
	}

	public void setShowAnotherBets( final boolean showAnotherBets ) {
		this.showAnotherBets = showAnotherBets;
	}

	public int getHomeTeamNumber() {
		return homeTeamNumber;
	}

	public void setHomeTeamNumber( final int homeTeamNumber ) {
		this.homeTeamNumber = homeTeamNumber;
	}

	@Override
	public String toString() {
		return String.format( "%s vs %s ( %d : %d )", team1, team2, score1, score2 );
	}
}
