package betmen.dto.dto;

import betmen.dto.serialization.DateTimeDeserializer;
import betmen.dto.serialization.DateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Min;
import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@ToString
public class MatchMessageDTO {

    private int id;

    @Min(value = 1, message = "errors.match_id_must_not_be_null")
    private int matchId;

    @NotEmpty(message = "errors.message_text_must_not_be_null")
    private String messageText;

    private UserDTO user;

    @JsonSerialize(using = DateTimeSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer.class)
    private LocalDateTime messageTime;
}
