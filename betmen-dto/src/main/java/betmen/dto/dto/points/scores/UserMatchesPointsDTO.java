package betmen.dto.dto.points.scores;

import betmen.dto.dto.UserDTO;
import betmen.dto.dto.points.UserMatchPointsHolderDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserMatchesPointsDTO {
    private UserDTO user;
    private List<UserMatchPointsHolderDTO> userMatchesPoints;
}
