package betmen.web.controllers.rest.activities;

import betmen.dto.dto.UsersRatingPositionDTO;
import betmen.dto.dto.MatchDTO;
import betmen.dto.dto.UserDTO;
import betmen.dto.serialization.DateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ActivityStreamDTO {

    private int activityStreamEntryTypeId;
    private UserDTO activityOfUser;
    private LocalDateTime activityTime;

    private MatchDTO match;

    private int score1;
    private int score2;

    private int oldScore1;
    private int oldScore2;

    private boolean showBetData;
    private boolean showOldScores;
    private UsersRatingPositionDTO activityBetPoints;
}
