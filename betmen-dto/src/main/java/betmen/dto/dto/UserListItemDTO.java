package betmen.dto.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserListItemDTO {
    private UserDTO user;
    private int betsCount;
}
