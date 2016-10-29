package betmen.dto.dto;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserRegResponse {
    private boolean success;
    private UserDTO user;
    private List<String> errors = Lists.newArrayList();

    public UserRegResponse() {
    }

    public UserRegResponse(final boolean success) {
        this.success = success;
    }
}
