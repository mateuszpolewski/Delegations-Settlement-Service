package pl.pracainz.delegacje.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import pl.pracainz.delegacje.model.User;
import pl.pracainz.delegacje.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomSuccessAuthenticationHandler extends SimpleUrlAuthenticationSuccessHandler
        implements AuthenticationSuccessHandler {

    @Autowired
    UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication auth) throws IOException, ServletException {
        User user = userService.getLoggedInUser();
        if (user.getPassword().isTemporary()){
            setDefaultTargetUrl("/auth/change-password");
        } else {
            setDefaultTargetUrl("/home");
        }
        super.onAuthenticationSuccess(request, response, auth);
    }

}
