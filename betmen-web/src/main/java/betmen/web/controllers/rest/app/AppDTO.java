package betmen.web.controllers.rest.app;

import betmen.dto.dto.UserDTO;
import betmen.dto.serialization.DateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString(of = {"projectName", "currentUser", "language"})
public class AppDTO {
    private final String projectName;
    private final LanguageDTO language;
    private UserDTO currentUser;

    @JsonSerialize(using = DateTimeSerializer.class)
    private LocalDateTime serverNow;

    public AppDTO(final String projectName, final LanguageDTO language) {
        this.projectName = projectName;
        this.language = language;
    }

}
