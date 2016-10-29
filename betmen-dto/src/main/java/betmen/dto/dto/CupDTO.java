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
@ToString(of = {"cupId", "cupName"})
@JsonIgnoreProperties(ignoreUnknown = true)
public class CupDTO {
    private int cupId;
    private String cupName;
    private CategoryDTO category;
    private int winnersCount;
    @JsonSerialize(using = DateTimeSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer.class)
    private LocalDateTime cupStartDate;
    private boolean readyForCupBets;
    private boolean readyForMatchBets;
    private boolean finished;
    private boolean cupStarted;
    private String logoUrl;
    private ValidationResultDto cupBetAllowance;

    public CupDTO(final int cupId, final String cupName, final CategoryDTO category) {
        this.cupId = cupId;
        this.cupName = cupName;
        this.category = category;
    }
}
