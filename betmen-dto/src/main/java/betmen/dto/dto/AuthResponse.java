package betmen.dto.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {

    public static final String AUTH_RESULT = "auth_result";
    public static final String USER_ID = "user_id";
    public static final String USER_NAME = "user_name";
    public static final String USER_ROLE = "user_role";
    public static final String ERROR = "error";

    private int responseCode;
    private Map<String, String> details;
}
