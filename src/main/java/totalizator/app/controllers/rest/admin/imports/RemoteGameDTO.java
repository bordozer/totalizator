package totalizator.app.controllers.rest.admin.imports;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties( ignoreUnknown = true )
public class RemoteGameDTO extends NotLoadedRemoteGameDTO {

	private String team1Name;
	private String team2Name;

//	private LocalDateTime beginningTime;
	private String beginningTime;

	private int score1;
	private int score2;

	private int homeTeamNumber;
	private boolean finished;

	public RemoteGameDTO() {

	}

	public RemoteGameDTO( final String remoteGameId ) {
		super( remoteGameId );
	}

	public String getTeam1Name() {
		return team1Name;
	}

	public void setTeam1Name( String team1Name ) {
		this.team1Name = team1Name;
	}

	public String getTeam2Name() {
		return team2Name;
	}

	public void setTeam2Name( String team2Name ) {
		this.team2Name = team2Name;
	}

	public String getBeginningTime() {
		return beginningTime;
	}

	public void setBeginningTime( final String beginningTime ) {
		this.beginningTime = beginningTime;
	}


	/*@JsonSerialize( using = DateTimeSerializer.class )
	public LocalDateTime getBeginningTime() {
		return beginningTime;
	}

	@JsonDeserialize( using = DateTimeDeserializer.class )
	public void setBeginningTime( LocalDateTime beginningTime ) {
		this.beginningTime = beginningTime;
	}*/

	public int getScore1() {
		return score1;
	}

	public void setScore1( int score1 ) {
		this.score1 = score1;
	}

	public int getScore2() {
		return score2;
	}

	public void setScore2( int score2 ) {
		this.score2 = score2;
	}

	public int getHomeTeamNumber() {
		return homeTeamNumber;
	}

	public void setHomeTeamNumber( final int homeTeamNumber ) {
		this.homeTeamNumber = homeTeamNumber;
	}

	public boolean isFinished() {
		return finished;
	}

	public void setFinished( boolean finished ) {
		this.finished = finished;
	}

	public final boolean isLoaded() {
		return true;
	}
}
