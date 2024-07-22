package codesquad.jspcafe.domain.user.controller;

import codesquad.jspcafe.domain.user.service.UserSignService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * UserSignUpServlet은 사용자 회원가입 요청을 처리하는 서블릿입니다. <br> GET 및 POST 메서드를 제공하여 각각 사용자 목록을 표시하고 새로운 사용자를
 * 생성합니다.
 */
@WebServlet("/users")
public class UserSignUpServlet extends HttpServlet {

    private UserSignService userSignService;

    /**
     * 서블릿을 초기화하여 서블릿 컨텍스트에서 UserSignService를 가져옵니다. 이 메서드는 서블릿이 처음 로드될 때 서블릿 컨테이너에 의해 호출됩니다.
     *
     * @throws ServletException 초기화 중 오류가 발생할 경우
     */
    @Override
    public void init() throws ServletException {
        userSignService = (UserSignService) getServletContext().getAttribute("userSignService");
    }

    /**
     * POST 요청을 처리하여 새로운 사용자를 생성한 후 /users 페이지로 리디렉션합니다. 클라이언트가 /users로 POST 요청을 보낼 때 이 메서드가 호출됩니다.
     *
     * @param req  클라이언트가 서블릿에 보낸 요청을 포함하는 HttpServletRequest 객체
     * @param resp 서블릿이 클라이언트에게 보내는 응답을 포함하는 HttpServletResponse 객체
     * @throws ServletException 서블릿이 POST 요청을 처리하는 동안 입력 또는 출력 오류가 발생할 경우
     * @throws IOException      서블릿이 POST 요청을 처리하는 동안 입력 또는 출력 오류가 감지될 경우
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
        Map<String, String[]> map = req.getParameterMap();
        userSignService.createUser(map);
        resp.sendRedirect("/users");
    }
}
