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
@ToString(of = {"pcsId", "strategyName"})
public class PointsCalculationStrategyDTO {
    private int pcsId;
    private String strategyName;
    private int pointsForMatchScore;
    private int pointsForMatchWinner;
    private int pointsDelta;
    private int pointsForBetWithinDelta;
}
