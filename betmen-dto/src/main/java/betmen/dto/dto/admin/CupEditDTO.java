package betmen.dto.dto.admin;

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
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@JsonIgnoreProperties(value = {
        "cupBetAllowance"
})
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString(of = {"cupId", "cupName"})
public class CupEditDTO {

    @Min(value = 0)
    private int cupId;

    @NotBlank(message = "errors.name_should_not_be_blank")
    @Size(min = 3, max = 100, message = "errors.name_has_wrong_length")
    private String cupName;

    @Min(value = 1)
    private int categoryId;

    @Min(value = 1)
    private int cupPointsCalculationStrategyId;

    @NotNull(message = "errors.cup_start_date_should_not_be_null")
    @JsonSerialize(using = DateTimeSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer.class)
    private LocalDateTime cupStartDate;

    @Min(value = 1)
    private int winnersCount;

    @Valid
    private List<CupWinnerEditDTO> cupWinners;

    private boolean finished;
    private boolean publicCup;
    private String cupImportId;

    // TODO: read only data
    private String logoUrl;
    private boolean readyForCupBets;
    private boolean readyForMatchBets;
}
