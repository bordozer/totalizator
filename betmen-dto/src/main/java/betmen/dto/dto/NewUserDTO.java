package betmen.dto.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString(of = {"login", "name"})
public class NewUserDTO {

    @Size(min = 5, max = 16, message = "errors.login_has_incorrect_length")
    @NotBlank(message = "errors.login_must_not_be_empty")
    private String login;

    @Size(min = 5, max = 100, message = "errors.name_has_incorrect_length")
    @NotBlank(message = "errors.username_must_not_be_empty")
    private String name;

    @Size(min = 8, max = 16, message = "errors.password_has_incorrect_length")
    @NotBlank(message = "errors.password_must_not_be_empty")
    private String password;
}
