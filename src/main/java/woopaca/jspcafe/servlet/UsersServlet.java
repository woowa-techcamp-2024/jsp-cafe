package woopaca.jspcafe.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import woopaca.jspcafe.resolver.RequestParametersResolver;
import woopaca.jspcafe.service.UserService;
import woopaca.jspcafe.servlet.dto.SignUpRequest;

import java.io.IOException;

@WebServlet("/users")
public class UsersServlet extends HttpServlet {

    private final Logger log = LoggerFactory.getLogger(UsersServlet.class);

    private final UserService userService = new UserService();

    /**
     * 회원가입 진행
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            SignUpRequest signUpRequest = RequestParametersResolver.resolve(request, SignUpRequest.class);
            userService.signUp(signUpRequest);
            response.sendRedirect("/users");
        } catch (IllegalArgumentException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (RuntimeException e) {
            log.error(e.getMessage(), e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "서버 오류");
        }
    }
}
