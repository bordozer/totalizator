package betmen.dto.dto.points;

import betmen.dto.dto.UserDTO;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

// TODO: looks similar for UsersRatingPositionDTO
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class UserMatchPointsHolderDTO {
    private UserDTO user;
    private String matchBetPoints;
    private String matchBonus;
    private String summaryPoints;
    private float summary; // TODO: hack for sorting

    public UserMatchPointsHolderDTO(final UserDTO user, final int matchBetPoints, final float matchBonus) {
        this.user = user;
        this.matchBetPoints = String.format("%d", matchBetPoints);
        this.matchBonus = String.format("%.2f", matchBonus);
        this.summaryPoints = String.format("%.2f", matchBetPoints + matchBonus);
        this.summary = matchBetPoints + matchBonus;
    }
}
