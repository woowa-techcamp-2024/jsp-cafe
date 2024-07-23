package codesquad.jspcafe.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * DefaultServlet은 .html 요청에 대한 처리를 진행하는 서블릿입니다. <br> GET 메서드를 제공하며 .html 요청에 대해 .jsp로 치환하여 데이터를
 * 전송합니다.
 */
@WebServlet("*.html")
public class DefaultServlet extends HttpServlet {

    /**
     * GET 요청을 처리하여 .html 을 .jsp 로 치환 및 포워딩합니다.
     *
     * @param req  an {@link HttpServletRequest} 클라이언트가 서블릿에 보낸 요청을 포함하는 HttpServletRequest 객체
     * @param resp an {@link HttpServletResponse} 서블릿이 클라이언트에게 보내는 응답을 포함하는 HttpServletResponse 객체
     * @throws ServletException 서블릿이 GET 요청을 처리하는 동안 입력 또는 출력 오류가 발생할 경우
     * @throws IOException      포워드 요청을 처리할 수 없는 경우
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
        String path = req.getRequestURI().replace(".html", ".jsp");
        req.getRequestDispatcher(path).forward(req, resp);
    }
}
