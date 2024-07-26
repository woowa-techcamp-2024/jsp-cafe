package codesquad.jspcafe.domain.user.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/**
 * UserLogoutServlet은 /users/logout URI 에 대한 요청을 처리하는 서블릿입니다. <br> POST 메서드를 제공하여 로그아웃을 수행합니다.
 */
@WebServlet("/users/logout")
public class UserLogoutServlet extends HttpServlet {

    /**
     * POST 요청을 처리하여 로그아웃을 수행합니다. <br> HttpSession을 조회해 세션을 invalidate 상태로 만듭니다.
     *
     * @param req  an {@link HttpServletRequest} 클라이언트가 서블릿에 보낸 요청을 포함하는 HttpServletRequest 객체
     * @param resp an {@link HttpServletResponse} 서블릿이 클라이언트에게 보내는 응답을 포함하는 HttpServletResponse 객체
     * @throws ServletException 서블릿이 POST 요청을 처리하는 동안 입력 또는 출력 오류가 발생할 경우
     * @throws IOException      리다이렉트 요청을 처리할 수 없는 경우
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        resp.sendRedirect("/index.html");
    }
}
