package betmen.web.security;

import betmen.core.entity.User;
import betmen.core.model.AppContext;
import betmen.core.service.SecurityService;
import betmen.core.service.UserService;
import betmen.core.translator.Language;
import betmen.core.translator.TranslatorService;
import betmen.dto.dto.AuthResponse;
import betmen.dto.utils.Parameters;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@Component
public class AuthSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Autowired
    private UserService userService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private TranslatorService translatorService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");

        final Language language = getLanguage(request);
        setLanguageCookie(response, language);
        AppContext.init(language, request.getSession());

        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();

        User user = userService.findByLogin(principal.getUsername());

        final boolean isAdmin = securityService.isAdmin(user);
        final String role = isAdmin ? "ROLE_ADMIN" : "ROLE_USER";

        Map<String, String> map = Maps.newLinkedHashMap();
        map.put(AuthResponse.AUTH_RESULT, "Logged in successfully");
        map.put(AuthResponse.USER_ID, String.valueOf(user.getId()));
        map.put(AuthResponse.USER_NAME, user.getUsername());
        map.put(AuthResponse.USER_ROLE, role);

        PrintWriter writer = response.getWriter();
        writer.write(new Gson().toJson(new AuthResponse(HttpStatus.SC_OK, map)));
        writer.flush();
    }

    private Language getLanguage(final HttpServletRequest request) {
        return translatorService.getLanguage(request.getParameter("language"));
    }

    private static void setLanguageCookie(final HttpServletResponse response, final Language language) {
        final Cookie languageCookie = new Cookie(Parameters.LANGUAGE_COOKIE_NAME, language.getCode());
        languageCookie.setMaxAge(Integer.MAX_VALUE);
        response.addCookie(languageCookie);
    }
}
