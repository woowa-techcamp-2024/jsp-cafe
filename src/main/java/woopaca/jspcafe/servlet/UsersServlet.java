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
import woopaca.jspcafe.servlet.dto.MembersResponse;
import woopaca.jspcafe.servlet.dto.SignUpRequest;

import java.io.IOException;
import java.util.List;

@WebServlet("/users")
public class UsersServlet extends HttpServlet {

    private final Logger log = LoggerFactory.getLogger(UsersServlet.class);

    private UserService userService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ServletContext servletContext = config.getServletContext();
        this.userService = (UserService) servletContext.getAttribute("userService");
    }

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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<MembersResponse> members = userService.getAllMembers();
        request.setAttribute("members", members);
        request.setAttribute("membersCount", members.size());
        request.getRequestDispatcher("/user/list.jsp")
                .forward(request, response);
    }
}
