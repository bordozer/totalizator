package betmen.web.controllers.rest.admin.imports;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
public class RemoteGameDTO {
    private String remoteGameId;
    private String team1Id;
    private String team1Name;
    private String team2Id;
    private String team2Name;
    private String beginningTime;
    private int score1;
    private int score2;
    private int homeTeamNumber;
    private boolean finished;
    private boolean loaded;
    @JsonIgnore
    private RemoteGameLocalData remoteGameLocalData;

    public RemoteGameDTO(final String remoteGameId) {
        this.remoteGameId = remoteGameId;
    }

    public String getRemoteGameId() {
        return remoteGameId;
    }

    public void setRemoteGameId(final String remoteGameId) {
        this.remoteGameId = remoteGameId;
    }

    public String getTeam1Id() {
        return team1Id;
    }

    public void setTeam1Id(final String team1Id) {
        this.team1Id = team1Id;
    }

    public String getTeam1Name() {
        return team1Name;
    }

    public void setTeam1Name(String team1Name) {
        this.team1Name = team1Name;
    }

    public String getTeam2Id() {
        return team2Id;
    }

    public void setTeam2Id(final String team2Id) {
        this.team2Id = team2Id;
    }

    public String getTeam2Name() {
        return team2Name;
    }

    public void setTeam2Name(String team2Name) {
        this.team2Name = team2Name;
    }

    public String getBeginningTime() {
        return beginningTime;
    }

    public void setBeginningTime(final String beginningTime) {
        this.beginningTime = beginningTime;
    }

    public int getScore1() {
        return score1;
    }

    public void setScore1(int score1) {
        this.score1 = score1;
    }

    public int getScore2() {
        return score2;
    }

    public void setScore2(int score2) {
        this.score2 = score2;
    }

    public int getHomeTeamNumber() {
        return homeTeamNumber;
    }

    public void setHomeTeamNumber(final int homeTeamNumber) {
        this.homeTeamNumber = homeTeamNumber;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public void setLoaded(final boolean loaded) {
        this.loaded = loaded;
    }

    @JsonProperty
    public RemoteGameLocalData getRemoteGameLocalData() {
        return remoteGameLocalData;
    }

    @JsonIgnore
    public void setRemoteGameLocalData(final RemoteGameLocalData remoteGameLocalData) {
        this.remoteGameLocalData = remoteGameLocalData;
    }
}
