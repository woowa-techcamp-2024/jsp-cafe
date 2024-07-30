package org.example.demo.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.demo.domain.User;
import org.example.demo.exception.NotFoundExceptoin;
import org.example.demo.model.UserCreateDao;
import org.example.demo.model.UserUpdateDao;
import org.example.demo.repository.UserRepository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class UserHandler {
    private static UserHandler instance;
    private UserRepository userRepository;

    private UserHandler() {
        userRepository = UserRepository.getInstance();
    }

    public static UserHandler getInstance() {
        if (instance == null) {
            instance = new UserHandler();
        }
        return instance;
    }

    public void handleUserList(HttpServletRequest request, HttpServletResponse response, List<String> pathVariables) throws ServletException, IOException {
        request.setAttribute("users", userRepository.getUsers());
        request.getRequestDispatcher("/WEB-INF/user/list.jsp").forward(request, response);
    }

    public void handleUpdateForm(HttpServletRequest request, HttpServletResponse response, List<String> pathVariables) throws ServletException, IOException {
        Long id = Long.parseLong(pathVariables.get(0));
        Optional<User> user = userRepository.getUser(id);
        if (user.isEmpty()) {
            request.setAttribute("error", "User not found");
            throw new NotFoundExceptoin("User not found");
        }

        request.setAttribute("user", user.get());
        request.getRequestDispatcher("/user/updateForm.jsp").forward(request, response);
    }

    public void handleUserProfile(HttpServletRequest request, HttpServletResponse response, List<String> pathVariables) throws ServletException, IOException {
        Long id = Long.parseLong(pathVariables.get(0));
        Optional<User> user = userRepository.getUser(id);

        if (user.isEmpty()) {
            request.setAttribute("error", "User not found");
            throw new NotFoundExceptoin("User not found");
        }

        request.setAttribute("user", user.get());
        request.getRequestDispatcher("/WEB-INF/user/profile.jsp").forward(request, response);
    }

    public void handleUserCreate(HttpServletRequest request, HttpServletResponse response, List<String> pathVariables) throws IOException {
        if (userRepository.getUserByUserId(request.getParameter("userId")).isPresent()) {
            request.setAttribute("error", "이미 존재하는 유저 아이디입니다.");
            throw new IllegalArgumentException();
        }

        UserCreateDao dao = new UserCreateDao(
                request.getParameter("userId"),
                request.getParameter("password"),
                request.getParameter("name"),
                request.getParameter("email")
        );
        userRepository.addUser(dao);
        response.sendRedirect("/users");
    }

    public void handleUserUpdate(HttpServletRequest request, HttpServletResponse response, List<String> pathVariables) throws IOException {
        Long id = Long.parseLong(pathVariables.get(0));
        User user = userRepository.getUser(id).orElseThrow(() -> new NotFoundExceptoin("User not found"));

        if (!user.getPassword().equals(request.getParameter("passwordCheck"))) {
            request.setAttribute("error", "패스워드가 안 맞아요~ 다시 확인해보세요");
            throw new IllegalArgumentException();
        }

        userRepository.updateUser(new UserUpdateDao(
                id,
                request.getParameter("password"),
                request.getParameter("name"),
                request.getParameter("email")
        ));

        response.sendRedirect("/users/" + id);
    }

    public void handleUserLogin(HttpServletRequest request, HttpServletResponse response, List<String> pathVariables) throws IOException, ServletException {
        String userId = request.getParameter("userId");
        String password = request.getParameter("password");

        Optional<User> user = userRepository.getUserByUserId(userId);
        if (user.isEmpty() || !user.get().getPassword().equals(password)) {
            request.setAttribute("error", "아이디 또는 패스워드가 틀렸습니다.");
            request.getRequestDispatcher("/WEB-INF/user/login_failed.jsp").forward(request, response);
            return;
        }

        request.getSession().setAttribute("user", user.get().getId());
        response.sendRedirect("/users/" + user.get().getId());
    }

    public void handleUserLogout(HttpServletRequest request, HttpServletResponse response, List<String> pathVariables) throws IOException {
        request.getSession().removeAttribute("user");
        response.sendRedirect("/");
    }

    public void handleUserLoginPage(HttpServletRequest request, HttpServletResponse response, List<String> pathVariables) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/user/login.jsp").forward(request, response);
    }

    public void handleUserFormPage(HttpServletRequest request, HttpServletResponse response, List<String> pathVariables) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/user/form.jsp").forward(request, response);
    }
}
