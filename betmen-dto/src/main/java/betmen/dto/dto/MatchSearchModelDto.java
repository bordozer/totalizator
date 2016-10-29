package betmen.dto.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Min;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class MatchSearchModelDto {

    @Min(value = 1)
    private int cupId;

    @Min(value = 0)
    private int userId;

    @Min(value = 0)
    private int teamId;

    @Min(value = 0)
    private int team2Id;

    private String filterByDate;
    private boolean filterByDateEnable;

    private boolean showFutureMatches;
    private boolean showFinished;

    private int sorting; // 1 is ASC, all remains are DESC

    // the field does not matter for backend
    private int categoryId;
}
