package betmen.dto.dto;

import betmen.dto.serialization.DateTimeDeserializer;
import betmen.dto.serialization.DateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@ToString(of = {"projectName", "currentUser", "language"})
public class AppDTO {

    private static final String PROJECT_NAME = "Betmen";
    private static final LanguageDTO DEFAULT_LANGUAGE = new LanguageDTO("English", "en");

    private String projectName;
    private LanguageDTO language;
    private UserDTO currentUser;

    @JsonSerialize(using = DateTimeSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer.class)
    private LocalDateTime serverNow;

    public AppDTO(final String projectName, final LanguageDTO language) {
        this.projectName = projectName;
        this.language = language;
    }
}
