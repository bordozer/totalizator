package betmen.dto.dto;

import betmen.dto.serialization.DateTimeDeserializer;
import betmen.dto.serialization.DateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString(of = {"cup", "matchesCount", "userBetsCount", "matchesWithoutBetsCount"})
public class MatchesAndBetsCollapsedDTO {

    private final CupDTO cup;
    private final UserDTO user;

    private int matchesCount;
    private int nowPlayingMatchesCount;
    private int userBetsCount;
    private int matchesWithoutBetsCount;

    @JsonSerialize(using = DateTimeSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer.class)
    private LocalDateTime firstMatchTime;

    @JsonSerialize(using = DateTimeSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer.class)
    private LocalDateTime firstMatchNoBetTime;

    private int todayMatchesCount;
    private int futureMatchesCount;
    private boolean cupWinnersBetsIsAccessible;
    private List<TeamDTO> userCupWinnersBets;
    private boolean cupHasWinners;

    public MatchesAndBetsCollapsedDTO(final CupDTO cupDTO, final UserDTO userDTO) {
        this.cup = cupDTO;
        this.user = userDTO;
    }
}
