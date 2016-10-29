package betmen.dto.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class BetDTO {
    private int matchBetId;
    private MatchDTO match;
    private UserDTO user;
    private int score1;
    private int score2;
    private boolean securedBet;

    public BetDTO(final MatchDTO match, final UserDTO user) {
        this.match = match;
        this.user = user;
    }
}
