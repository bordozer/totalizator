package betmen.dto.dto.admin;

import betmen.dto.dto.TeamDTO;
import betmen.dto.serialization.DateTimeDeserializer;
import betmen.dto.serialization.DateTimeSerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString(of = {"matchId", "team1Id", "team2Id"})
@JsonIgnoreProperties(ignoreUnknown = true)
public class MatchEditDTO {

    @Min(value = 0)
    private int matchId;

    @Min(value = 0)
    private int categoryId; // TODO: it looks like this var just keeps state for FE

    @Min(value = 1, message = "errors.cup_should_be_provided")
    private int cupId;

    @Min(value = 1, message = "errors.team1_should_be_provided")
    private int team1Id;

    @Min(value = 1, message = "errors.team2_should_be_provided")
    private int team2Id;

    private int score1;
    private int score2;

    @NotNull(message = "errors.beginning_time_must_not_be_null")
    private LocalDateTime beginningTime;

    private boolean matchFinished;
    private int homeTeamNumber;
    private String matchDescription;
    private String remoteGameId;

    // TODO: read only data (bad design)

    @JsonIgnore
    private TeamDTO team1;

    @JsonIgnore
    private TeamDTO team2;

    private int betsCount;

    public int getMatchId() {
        return matchId;
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public LocalDateTime getBeginningTime() {
        return beginningTime;
    }

    @JsonDeserialize(using = DateTimeDeserializer.class)
    public void setBeginningTime(final LocalDateTime beginningTime) {
        this.beginningTime = beginningTime;
    }

    @JsonProperty
    public TeamDTO getTeam1() {
        return team1;
    }

    @JsonIgnore
    public void setTeam1(final TeamDTO team1) {
        this.team1 = team1;
    }

    @JsonProperty
    public TeamDTO getTeam2() {
        return team2;
    }

    @JsonIgnore
    public void setTeam2(final TeamDTO team2) {
        this.team2 = team2;
    }
}
