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
        request.getRequestDispatcher("/user/list.jsp").forward(request, response);
    }

    public void handleUpdateForm(HttpServletRequest request, HttpServletResponse response, List<String> pathVariables) throws ServletException, IOException {
        Long id = Long.parseLong(pathVariables.get(0));
        User user = userRepository.getUser(id).orElseThrow(() -> new NotFoundExceptoin("User not found"));
        request.setAttribute("user", user);
        request.getRequestDispatcher("/user/updateForm.jsp").forward(request, response);
    }

    public void handleUserProfile(HttpServletRequest request, HttpServletResponse response, List<String> pathVariables) throws ServletException, IOException {
        Long id = Long.parseLong(pathVariables.get(0));
        User user = userRepository.getUser(id).orElseThrow(() -> new NotFoundExceptoin("User not found"));
        request.setAttribute("user", user);
        request.getRequestDispatcher("/user/profile.jsp").forward(request, response);
    }

    public void handleUserCreate(HttpServletRequest request, HttpServletResponse response, List<String> pathVariables) throws IOException {
        if (userRepository.getUserByUserId(request.getParameter("userId")).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 유저 아이디입니다.");
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
            throw new IllegalArgumentException("패스워드가 안 맞아요~ 다시 확인해보세용");
        }

        userRepository.updateUser(new UserUpdateDao(
                id,
                request.getParameter("password"),
                request.getParameter("name"),
                request.getParameter("email")
        ));

        response.sendRedirect("/users/" + id);
    }

}
