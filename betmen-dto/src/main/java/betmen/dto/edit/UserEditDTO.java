package betmen.dto.edit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode(of = {"userId"})
@ToString(of = {"userId", "login", "userName"})
public class UserEditDTO {

    private int userId;
    private String login;

    @NotBlank
    @Size(min = 3)
    private String userName;
}
