package org.example.cafe.servlet;

import static org.example.cafe.utils.LoggerFactory.getLogger;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import org.example.cafe.domain.user.User;
import org.example.cafe.domain.user.UserRepository;
import org.slf4j.Logger;

@WebServlet(name = "UserServlet", value = "/users")
public class UserServlet extends HttpServlet {

    private static final Logger log = getLogger(UserServlet.class);

    private UserRepository userRepository;

    @Override
    public void init() {
        this.userRepository = (UserRepository) getServletContext().getAttribute("UserRepository");
        log.debug("Init servlet: {}", this.getClass().getSimpleName());
    }

    /**
     * 회원 목록 조회 페이지를 반환한다.
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");

        List<User> users = userRepository.findAll();

        request.setAttribute("users", users);
        request.getRequestDispatcher("/user/list.jsp").forward(request, response);
    }

    /**
     * 회원 가입한다.
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");

        String userId = request.getParameter("userId");
        String password = request.getParameter("password");
        String nickname = request.getParameter("nickname");
        String email = request.getParameter("email");

        User user = new User(userId, password, nickname, email);
        userRepository.save(user);

        response.sendRedirect("/users");
    }
}