package betmen.dto.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode(of = "userId")
@ToString
public class UserDTO {
    private int userId;
    private String userName;
}
