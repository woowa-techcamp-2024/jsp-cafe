package codesquad.jspcafe.domain.user.controller;

import codesquad.jspcafe.domain.user.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * UserListServlet은 /users URI 에 대한 요청을 처리하는 서블릿입니다. <br> GET 메서드를 제공하여 사용자 목록을 표시합니다.
 */
@WebServlet("/users")
public class UserListServlet extends HttpServlet {

    private transient UserService userService;

    /**
     * 서블릿을 초기화하여 서블릿 컨텍스트에서 UserService를 가져옵니다. 이 메서드는 서블릿이 처음 로드될 때 서블릿 컨테이너에 의해 호출됩니다.
     *
     * @throws ServletException 초기화 중 오류가 발생할 경우
     */
    @Override
    public void init() throws ServletException {
        this.userService = (UserService) getServletContext().getAttribute("userService");
    }

    /**
     * GET 요청을 처리하여 userList.jsp 페이지로 포워딩합니다. <br> 클라이언트가 /users로 GET 요청을 보낼 때 이 메서드가 호출됩니다. <br>
     * UserService에서 사용자 리스트를 조회해서 Request에 attribute로 저장합니다.
     *
     * @param req  an {@link HttpServletRequest} 클라이언트가 서블릿에 보낸 요청을 포함하는 HttpServletRequest 객체
     * @param resp an {@link HttpServletResponse} 서블릿이 클라이언트에게 보내는 응답을 포함하는 HttpServletResponse 객체
     * @throws ServletException 서블릿이 GET 요청을 처리하는 동안 입력 또는 출력 오류가 발생할 경우
     * @throws IOException      포워드 요청을 처리할 수 없는 경우
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
        req.setAttribute("userList", userService.findAllUser());
        req.getRequestDispatcher("/WEB-INF/jsp/userList.jsp").forward(req, resp);
    }
}
