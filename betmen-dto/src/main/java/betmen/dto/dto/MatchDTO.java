package betmen.dto.dto;

import betmen.dto.serialization.DateTimeDeserializer;
import betmen.dto.serialization.DateTimeSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
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
    private String description;

    @JsonSerialize(using = DateTimeSerializer.class)
    public LocalDateTime getBeginningTime() {
        return beginningTime;
    }

    @JsonDeserialize(using = DateTimeDeserializer.class)
    public void setBeginningTime(final LocalDateTime beginningTime) {
        this.beginningTime = beginningTime;
    }
}
