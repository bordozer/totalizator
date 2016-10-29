package betmen.rests.utils.data.builders;

import betmen.dto.dto.admin.MatchEditDTO;

import java.time.LocalDateTime;

public class MatchEditDtoBuilder {
    private int matchId;
    private int cupId;
    private int team1Id;
    private int team2Id;
    private int score1;
    private int score2;
    private LocalDateTime beginningTime;
    private boolean finished;
    private int homeTeamNumber;
    private String description;
    private String remoteGameId;

    public MatchEditDtoBuilder withId(final int matchId) {
        this.matchId = matchId;
        return this;
    }

    public MatchEditDtoBuilder withCupId(final int cupId) {
        this.cupId = cupId;
        return this;
    }

    public MatchEditDtoBuilder withTeam1Id(final int team1Id) {
        this.team1Id = team1Id;
        return this;
    }

    public MatchEditDtoBuilder withTeam2Id(final int team2Id) {
        this.team2Id = team2Id;
        return this;
    }

    public MatchEditDtoBuilder withScore1(final int score1) {
        this.score1 = score1;
        return this;
    }

    public MatchEditDtoBuilder withScore2(final int score2) {
        this.score2 = score2;
        return this;
    }

    public MatchEditDtoBuilder withHomeTeamNumber(final int homeTeamNumber) {
        this.homeTeamNumber = homeTeamNumber;
        return this;
    }

    public MatchEditDtoBuilder firstTeamWin() {
        this.score1 = 2;
        this.score2 = 1;
        return this;
    }

    public MatchEditDtoBuilder secondTeamWin() {
        this.score1 = 1;
        this.score2 = 2;
        return this;
    }

    public MatchEditDtoBuilder withDraw() {
        this.score1 = 2;
        this.score2 = 2;
        return this;
    }

    public MatchEditDtoBuilder startingAt(final LocalDateTime beginningTime) {
        this.beginningTime = beginningTime;
        return this;
    }

    public MatchEditDtoBuilder withBeginningTime(final LocalDateTime withBeginningTime) {
        this.beginningTime = withBeginningTime;
        return this;
    }

    public MatchEditDtoBuilder finished(final boolean finished) {
        this.finished = finished;
        return this;
    }

    public MatchEditDtoBuilder withDescription(final String description) {
        this.description = description;
        return this;
    }

    public MatchEditDtoBuilder withImportId(final String remoteGameId) {
        this.remoteGameId = remoteGameId;
        return this;
    }

    public MatchEditDTO build() {
        MatchEditDTO dto = new MatchEditDTO();
        dto.setMatchId(matchId);
        dto.setCupId(cupId);
        dto.setBeginningTime(beginningTime);
        dto.setTeam1Id(team1Id);
        dto.setTeam2Id(team2Id);
        dto.setScore1(score1);
        dto.setScore2(score2);
        dto.setHomeTeamNumber(homeTeamNumber);
        dto.setMatchDescription(description);
        dto.setMatchFinished(finished);
        dto.setRemoteGameId(remoteGameId);
        return dto;
    }
}
