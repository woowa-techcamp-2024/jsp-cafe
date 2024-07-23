package org.example.servlet.view;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.data.UserDataHandler;
import org.example.data.UserDataHandlerInMemory;
import org.example.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

@WebServlet("/users")
public class UserListView extends HttpServlet {
    private final Logger log = LoggerFactory.getLogger(UserListView.class);
    private UserDataHandler userDataHandler;

    @Override
    public void init() throws ServletException {
        super.init();
        userDataHandler = (UserDataHandlerInMemory) getServletContext().getAttribute("userDataHandlerInMemory");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        log.debug("[UserListView] called");
        List<User> users = userDataHandler.findAll();
        request.setAttribute("users", users);
        request.getRequestDispatcher("/user/list.jsp").forward(request, response);
    }
}
