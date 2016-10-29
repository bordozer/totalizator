package betmen.dto.dto;

import betmen.dto.serialization.DateDeserializer;
import betmen.dto.serialization.DateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class MatchBetsOnDateDTO {

    private LocalDate date;
    private List<MatchBetDTO> matchBets;

    @JsonSerialize(using = DateSerializer.class)
    public LocalDate getDate() {
        return date;
    }

    @JsonDeserialize(using = DateDeserializer.class)
    public void setDate(final LocalDate date) {
        this.date = date;
    }
}
