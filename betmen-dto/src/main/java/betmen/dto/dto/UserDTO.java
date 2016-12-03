package betmen.dto.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "userId")
@ToString
public class UserDTO {
    private int userId;
    private String userName;
}
