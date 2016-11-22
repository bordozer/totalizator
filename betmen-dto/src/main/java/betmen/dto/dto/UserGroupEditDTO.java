package betmen.dto.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;

import java.util.List;

@Getter
@Setter
@ToString(of = {"userGroupId", "userGroupName"})
public class UserGroupEditDTO {
    private int userGroupId;
    @NotBlank
    private String userGroupName;
    private List<Integer> cupIds;
}
