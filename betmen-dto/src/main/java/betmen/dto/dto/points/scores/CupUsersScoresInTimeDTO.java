package betmen.dto.dto.points.scores;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CupUsersScoresInTimeDTO {
    private List<String> dates;
    private Map<Integer, UserMatchesPointsDTO> usersPointsMap;

}
