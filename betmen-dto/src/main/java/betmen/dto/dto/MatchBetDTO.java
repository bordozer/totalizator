package betmen.dto.dto;

import betmen.dto.dto.points.UserMatchPointsHolderDTO;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class MatchBetDTO {
    private int matchId;
    private MatchDTO match;
    private BetDTO bet;
    private UserMatchPointsHolderDTO userMatchPointsHolder;
    private boolean bettingAllowed;
    private String bettingValidationMessage;
    private int totalBets;

    public MatchBetDTO(final MatchDTO match) {
        this.matchId = match.getMatchId();
        this.match = match;
    }
}
