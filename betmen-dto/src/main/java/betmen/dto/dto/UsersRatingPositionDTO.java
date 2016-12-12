package betmen.dto.dto;

import betmen.dto.dto.UserDTO;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class UsersRatingPositionDTO {
    private UserDTO user;
    private String betPoints;
    private String matchBonus;
    private String pointsSummary;

    public UsersRatingPositionDTO(final UserDTO user, final int betPoints, final float matchBonus) {
        this.user = user;
        this.betPoints = String.format("%d", betPoints);
        this.matchBonus = String.format("%.2f", matchBonus);
        this.pointsSummary = String.format("%.2f", betPoints + matchBonus);
    }
}
