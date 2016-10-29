package betmen.dto.dto.points.scores;

import betmen.dto.dto.UserDTO;
import betmen.dto.dto.points.UserCupPointsHolderDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRatingPositionDTO {
    private UserDTO user;
    private UserCupPointsHolderDTO userCupPointsHolder;
}
