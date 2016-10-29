package betmen.web.security;

import betmen.dto.dto.AuthResponse;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import org.apache.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@Component
public class AuthFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");

        Map<String, String> map = Maps.newLinkedHashMap();
        map.put(AuthResponse.AUTH_RESULT, "Authentication failure");
        map.put(AuthResponse.ERROR, exception.getMessage()); // TODO: not for production

        PrintWriter writer = response.getWriter();
        writer.write(new Gson().toJson(new AuthResponse(HttpStatus.SC_UNAUTHORIZED, map)));
        writer.flush();
    }
}
