package betmen.dto.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class UserListItemDTO {
    private UserDTO user;
    private int betsCount;
}
