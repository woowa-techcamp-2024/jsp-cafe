package camp.woowa.jspcafe.servlet;

import camp.woowa.jspcafe.core.ServiceLocator;
import camp.woowa.jspcafe.exception.CustomException;
import camp.woowa.jspcafe.exception.HttpStatus;
import camp.woowa.jspcafe.model.User;
import camp.woowa.jspcafe.service.UserService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "usersServlet", urlPatterns = {"/users/*"})
public class UsersServlet extends HttpServlet {
    private final UserService userService;

    public UsersServlet() {
        userService = ServiceLocator.getService(UserService.class);
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();

        if (pathInfo == null || "".equalsIgnoreCase(pathInfo)) {
            List<User> users = userService.findAll();
            req.setAttribute("users", users); // user 리스트 정보를 가져옴
            RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/jsp/user/list.jsp");

            try {
                dispatcher.forward(req, resp);
            } catch (ServletException | IOException e) {
                log(e.getMessage());
            }
        } else if (pathInfo.endsWith("/form")) { // POST /users/{id}/form 필터링
            String[] split = pathInfo.split("/");
            long id = 0;
            try { // id 가 long인지 확인
                id = Long.parseLong(split[1]);
            } catch (NumberFormatException e) {
                log(e.getMessage());
                throw new CustomException(HttpStatus.BAD_REQUEST, "Invalide User Id");
            }
            User user = userService.findById(id);
            req.setAttribute("user", user);
            RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/jsp/user/updateForm.jsp");

            try {
                dispatcher.forward(req, resp);
            } catch (ServletException | IOException e) {
                log(e.getMessage());
            }
        } else if (pathInfo.split("/").length == 2) { // POST /users/{id} 필터링
            try {
                User user = userService.findById(Long.parseLong(pathInfo.substring(1)));
                req.setAttribute("user", user);
            } catch (NumberFormatException e) {
                log(e.getMessage());
                throw new CustomException(HttpStatus.BAD_REQUEST);
            }

            RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/jsp/user/profile.jsp");

            try {
                dispatcher.forward(req, resp);
            } catch (ServletException | IOException e) {
                log(e.getMessage());
            }
        } else {
            throw new CustomException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();

        if (pathInfo == null || "/".equalsIgnoreCase(pathInfo) || "".equalsIgnoreCase(pathInfo)) { // POST /users 필터링
            userService.createUser(req.getParameter("userId"),
                    req.getParameter("password"),
                    req.getParameter("name"),
                    req.getParameter("email"));
            try {
                res.sendRedirect("/users");
            } catch (IOException e) {
                log(e.getMessage());
            }
        } else if (pathInfo.equalsIgnoreCase("/login")) {
            String userId = req.getParameter("userId");
            String password = req.getParameter("password");

            User user = userService.login(userId, password);
            req.getSession().setAttribute("user", user);

            try {
                res.sendRedirect("/");
            } catch (IOException e) {
                log(e.getMessage());
            }
        } else if (pathInfo.endsWith("/form")) { // POST /users/{id}/form 필터링
            String[] split = pathInfo.split("/");
            long id = 0;
            try { // id 가 long인지 확인
                id = Long.parseLong(split[1]);
            } catch (NumberFormatException e) {
                log(e.getMessage());
            }


            HttpSession session = req.getSession(false);

            if (session == null) {
                throw new CustomException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
            }

            User sessionUser = (User) session.getAttribute("user");

            userService.update(sessionUser, id,
                    req.getParameter("password"),
                    req.getParameter("name"),
                    req.getParameter("email"));

            try {
                res.sendRedirect("/users/" + id + "/form");
            } catch (IOException e) {
                log(e.getMessage());
            }
        } else if (pathInfo.equalsIgnoreCase("/logout")) {
            req.getSession().invalidate();
            try {
                res.sendRedirect("/");
            } catch (IOException e) {
                log(e.getMessage());
            }
        } else {
            throw new CustomException(HttpStatus.NOT_FOUND);
        }
    }
}
