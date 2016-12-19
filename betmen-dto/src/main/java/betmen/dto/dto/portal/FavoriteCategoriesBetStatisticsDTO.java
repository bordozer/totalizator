package betmen.dto.dto.portal;

import betmen.dto.serialization.DateDeserializer;
import betmen.dto.serialization.DateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class FavoriteCategoriesBetStatisticsDTO {
    @JsonSerialize(using = DateSerializer.class)
    @JsonDeserialize(using = DateDeserializer.class)
    private LocalDate onDate;
    private List<FavoriteCategoryBetStatisticsDTO> categoryBetStatistics;
}
