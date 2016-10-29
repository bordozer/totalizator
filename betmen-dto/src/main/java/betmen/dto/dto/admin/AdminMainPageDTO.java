package betmen.dto.dto.admin;

import betmen.dto.dto.UserDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class AdminMainPageDTO {
    private int id = 0;
    private UserDTO userDTO;
}
