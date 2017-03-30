package betmen.core.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@ToString
public class MatchMessage {
    private int id;
    private int matchId;
    private int userId;
    private LocalDateTime messageTime;
    private String messageText;
}
