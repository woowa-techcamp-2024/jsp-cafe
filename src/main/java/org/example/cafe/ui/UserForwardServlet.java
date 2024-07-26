package org.example.cafe.ui;

import static org.example.cafe.utils.LoggerFactory.getLogger;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.slf4j.Logger;

@WebServlet(name = "UserForwardServlet", value = "/user/*")
public class UserForwardServlet extends HttpServlet {

    private static final Logger log = getLogger(UserForwardServlet.class);

    @Override
    public void init() {
        log.debug("Init servlet: {}", this.getClass().getSimpleName());
    }

    /**
     * 회원 프로필 페이지를 반환한다..
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");

        String requestURI = request.getRequestURI();

        if (requestURI.equals("/user/regist")) {
            forward("/WEB-INF/user/regist.jsp", request, response);
            return;
        }
    }

    private void forward(String path, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher(path).forward(request, response);
    }
}