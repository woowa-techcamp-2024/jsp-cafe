package codesquad.jspcafe.domain.user.controller;

import codesquad.jspcafe.domain.user.payload.request.UserLoginRequest;
import codesquad.jspcafe.domain.user.payload.response.UserSessionResponse;
import codesquad.jspcafe.domain.user.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/**
 * UserLoginServlet은 /users/login URI 에 대한 요청을 처리하는 서블릿입니다. <br> GET, POST 메서드를 제공하여 GET 요청시 로그인
 * 페이지로 이동합니다. POST 요청시 로그인을 수행합니다.
 */
@WebServlet("/users/login")
public class UserLoginServlet extends HttpServlet {

    private transient UserService userService;

    /**
     * 서블릿을 초기화하여 서블릿 컨텍스트에서 UserService를 가져옵니다. 이 메서드는 서블릿이 처음 로드될 때 서블릿 컨테이너에 의해 호출됩니다.
     *
     * @throws ServletException 초기화 중 오류가 발생할 경우
     */
    @Override
    public void init() throws ServletException {
        userService = (UserService) getServletContext().getAttribute("userService");
    }

    /**
     * GET 요청을 처리하여 로그인 페이지로 포워딩합니다.
     *
     * @param req  an {@link HttpServletRequest} 클라이언트가 서블릿에 보낸 요청을 포함하는 HttpServletRequest 객체
     * @param resp an {@link HttpServletResponse} 서블릿이 클라이언트에게 보내는 응답을 포함하는 HttpServletResponse 객체
     * @throws ServletException 서블릿이 GET 요청을 처리하는 동안 입력 또는 출력 오류가 발생할 경우
     * @throws IOException      포워드 요청을 처리할 수 없는 경우
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/jsp/userLogin.jsp").forward(req, resp);
    }

    /**
     * POST 요청을 처리하여 로그인을 수행합니다.
     *
     * @param req  an {@link HttpServletRequest} 클라이언트가 서블릿에 보낸 요청을 포함하는 HttpServletRequest 객체
     * @param resp an {@link HttpServletResponse} 서블릿이 클라이언트에게 보내는 응답을 포함하는 HttpServletResponse 객체
     * @throws ServletException 서블릿이 POST 요청을 처리하는 동안 입력 또는 출력 오류가 발생할 경우
     * @throws IOException      리다이렉트 요청을 처리할 수 없는 경우
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
        UserSessionResponse sessionResponse = userService.loginUser(
            UserLoginRequest.from(req.getParameterMap()));
        HttpSession oldSession = req.getSession(false);
        if (oldSession != null) {
            oldSession.invalidate();
        }
        req.getSession(true).setAttribute("user", sessionResponse);
        resp.sendRedirect("/index.html");
    }
}
