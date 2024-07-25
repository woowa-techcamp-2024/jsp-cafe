package codesquad.jspcafe.domain.user.controller;

import codesquad.jspcafe.domain.user.payload.request.UserUpdateRequest;
import codesquad.jspcafe.domain.user.payload.response.UserCommonResponse;
import codesquad.jspcafe.domain.user.payload.response.UserSessionResponse;
import codesquad.jspcafe.domain.user.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * UserInfoServlet은 /users/* URI 에 대한 요청을 처리하는 서블릿입니다. <br> GET 메서드를 제공하여 사용자 정보를 표시하거나 사용자 정보 업데이트
 * 페이지로 이동합니다.
 */
@WebServlet("/users/*")
public class UserInfoServlet extends HttpServlet {

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
     * GET 요청을 처리하여 여러 .jsp 페이지로 포워딩합니다.<br> 클라이언트가 /users/{userId}로 GET 요청을 보낼 때,
     * /users/{userId}/form으로 GET 요청을 보낼 때 이 메서드가 호출됩니다. <br><br> /users/{userId}: 사용자 정보를
     * 페이지(userProfile.jsp)로 포워딩합니다. <br> /users/{userId}/form: 사용자 정보 갱신 페이지(userUpdateForm.jsp)로
     * 포워딩합니다. <br>
     *
     * @param req  an {@link HttpServletRequest} 클라이언트가 서블릿에 보낸 요청을 포함하는 HttpServletRequest 객체
     * @param resp an {@link HttpServletResponse} 서블릿이 클라이언트에게 보내는 응답을 포함하는 HttpServletResponse 객체
     * @throws ServletException 서블릿이 GET 요청을 처리하는 동안 입력 또는 출력 오류가 발생할 경우
     * @throws IOException      포워드 요청을 처리할 수 없는 경우
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.isBlank()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid userId");
            return;
        }
        // Case for /users/*/form
        if (pathInfo.endsWith("/form")) {
            String userId = pathInfo.substring(1, pathInfo.length() - "/form".length());
            UserCommonResponse userCommonResponse = userService.getUserById(userId);
            req.setAttribute("userId", userCommonResponse.getUserId());
            req.getRequestDispatcher("/WEB-INF/jsp/userUpdateForm.jsp").forward(req, resp);
        } else {
            String userId = pathInfo.substring(1);
            UserCommonResponse userCommonResponse = userService.getUserById(userId);
            req.setAttribute("user", userCommonResponse);
            req.getRequestDispatcher("/WEB-INF/jsp/userProfile.jsp").forward(req, resp);
        }
    }

    /**
     * POST 요청을 처리하여 사용자 정보를 갱신한 후 /users/{userId} 페이지로 리디렉션합니다.<br> 클라이언트가 /users/{userId}/form으로
     * POST 요청을 보낼 때 이 메서드가 호출됩니다.
     *
     * @param req  an {@link HttpServletRequest} 클라이언트가 서블릿에 보낸 요청을 포함하는 HttpServletRequest 객체
     * @param resp an {@link HttpServletResponse} 서블릿이 클라이언트에게 보내는 응답을 포함하는 HttpServletResponse 객체
     * @throws ServletException 서블릿이 POST 요청을 처리하는 동안 입력 또는 출력 오류가 발생할 경우
     * @throws IOException      서블릿이 POST 요청을 처리하는 동안 입력 또는 출력 오류가 감지될 경우
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (!pathInfo.endsWith("/form")) {
            resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "Invalid request");
            return;
        }
        Map<String, String[]> parameterMap = req.getParameterMap();
        UserSessionResponse userSessionResponse = (UserSessionResponse) req.getSession()
            .getAttribute("user");
        if (userSessionResponse == null || !(userSessionResponse.getUserId()
            .equals(parameterMap.get("userId")[0]))) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden");
            return;
        }
        UserUpdateRequest userUpdateRequest = UserUpdateRequest.from(parameterMap);
        userService.updateUserInfo(userUpdateRequest);
        resp.sendRedirect("/users/" + parameterMap.get("userId")[0]);
    }
}
