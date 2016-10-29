package betmen.dto.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TeamCardCupData {
    private CupDTO cup;
    private CupWinnerDTO cupWinner;
    private int finishedMatchCount;
    private int wonMatchCount;
    private int futureMatchesCount;

    public TeamCardCupData(final CupDTO cup) {
        this.cup = cup;
    }
}
