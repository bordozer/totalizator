package betmen.web.controllers.rest.user.groups;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@ToString(of = {"userGroupId", "userGroupName"})
public class UserGroupEditDTO {
    private int userGroupId;
    private String userGroupName;
    private List<Integer> cupIds;
}
