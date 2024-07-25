package woopaca.jspcafe.servlet;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
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
import java.util.Map;

@WebServlet("/users/signup")
public class SignUpServlet extends HttpServlet {

    private final Logger log = LoggerFactory.getLogger(SignUpServlet.class);

    private UserService userService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ServletContext servletContext = config.getServletContext();
        this.userService = (UserService) servletContext.getAttribute("userService");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/user/register.jsp")
                .forward(request, response);
    }

    /**
     * 회원가입 진행
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            Map<String, String[]> parameters = request.getParameterMap();
            SignUpRequest signUpRequest = RequestParametersResolver.resolve(parameters, SignUpRequest.class);
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
