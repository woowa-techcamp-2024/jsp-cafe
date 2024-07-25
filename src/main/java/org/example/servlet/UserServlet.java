package org.example.servlet;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import org.example.dto.UserCreateReqDto;
import org.example.entity.User;
import org.example.service.UserService;

@WebServlet("/users")
public class UserServlet extends HttpServlet {

    UserService userService = new UserService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userId = request.getParameter("userId");
        String password = request.getParameter("password");
        String name = request.getParameter("name");
        String email = request.getParameter("email");

        // 여기서 폼 데이터를 처리하고, 데이터베이스에 저장하거나 다른 작업을 수행할 수 있습니다.

        // 예시로, 콘솔에 폼 데이터 출력
        System.out.println("User ID: " + userId);
        System.out.println("Password: " + password);
        System.out.println("Name: " + name);
        System.out.println("Email: " + email);
        userService.createUser(new UserCreateReqDto(userId, password, name, email));

        // 처리 후 리다이렉션 또는 응답 작성
        response.sendRedirect("/users");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // GET 요청 처리
        // 회원 목록 조회
        List<User> users = userService.getAllUsers();

        request.setAttribute("users", users);

        // list.jsp로 포워드
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/user/list.jsp");
        dispatcher.forward(request, response);
    }
}

