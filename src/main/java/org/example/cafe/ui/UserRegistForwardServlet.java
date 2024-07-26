package org.example.cafe.ui;

import static org.example.cafe.utils.LoggerFactory.getLogger;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.slf4j.Logger;

@WebServlet(name = "UserRegistForwardServlet", value = "/user/regist")
public class UserRegistForwardServlet extends HttpServlet {

    private static final Logger log = getLogger(UserRegistForwardServlet.class);

    @Override
    public void init() {
        log.debug("Init servlet: {}", this.getClass().getSimpleName());
    }

    /**
     * 회원 가입 페이지를 반환한다.
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");

        request.getRequestDispatcher("/WEB-INF/user/regist.jsp").forward(request, response);
    }
}