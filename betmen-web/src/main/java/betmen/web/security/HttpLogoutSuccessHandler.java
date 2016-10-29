package betmen.web.security;

import betmen.core.entity.User;
import betmen.core.service.UserService;
import betmen.dto.dto.AuthResponse;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@Component
public class HttpLogoutSuccessHandler implements LogoutSuccessHandler {

    @Autowired
    private UserService userService;

    @Override
    public void onLogoutSuccess(final HttpServletRequest request, final HttpServletResponse response,
                                final Authentication authentication) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");

        Map<String, String> map = Maps.newLinkedHashMap();
        map.put(AuthResponse.AUTH_RESULT, "Logged out");
        map.put(AuthResponse.USER_NAME, getUserName(authentication));


        PrintWriter writer = response.getWriter();
        writer.write(new Gson().toJson(new AuthResponse(HttpStatus.SC_OK, map)));
        writer.flush();
    }

    private String getUserName(final Authentication authentication) {
        if (authentication == null) {
            return StringUtils.EMPTY;
        }

        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
        User user = userService.findByLogin(principal.getUsername());
        return user.getUsername();
    }
}
