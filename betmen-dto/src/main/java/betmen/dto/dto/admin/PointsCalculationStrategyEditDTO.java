package betmen.dto.dto.admin;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString(of = {"pcsId", "strategyName"})
public class PointsCalculationStrategyEditDTO {

    @Min(value = 0)
    private int pcsId;

    @NotBlank(message = "errors.name_should_not_be_blank")
    @Size(min = 3, max = 100, message = "errors.name_has_wrong_length")
    private String strategyName;

    @Min(value = 1)
    private int pointsForMatchScore;

    @Min(value = 1)
    private int pointsForMatchWinner;

    private int pointsDelta;

    private int pointsForBetWithinDelta;
}
