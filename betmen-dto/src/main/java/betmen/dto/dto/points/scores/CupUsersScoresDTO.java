package betmen.dto.dto.points.scores;

import betmen.dto.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CupUsersScoresDTO {
    private UserDTO currentUser;
    private List<UserRatingPositionDTO> userRatingPositions;
}
