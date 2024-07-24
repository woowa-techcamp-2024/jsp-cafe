package cafe.service;

import cafe.domain.db.UserDatabase;
import cafe.domain.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;

public class UserService {
    private final UserDatabase userDatabase;

    public UserService(UserDatabase userDatabase) {
        this.userDatabase = userDatabase;
    }

    public void save(HttpServletRequest req, HttpServletResponse resp) {
        String id = req.getParameter("userId");
        String password = req.getParameter("password");
        String name = req.getParameter("name");
        String email = req.getParameter("email");

        userDatabase.save(User.of(id, password, name, email));
    }

    public User find(HttpServletRequest req, HttpServletResponse resp) {
        String uri = req.getRequestURI();
        String id = uri.split("/")[2];
        req.setAttribute("id", id);

        User user = userDatabase.find(id);
        if (user == null) throw new IllegalArgumentException("User not found!");
        return user;
    }

    public Map<String, User> findAll(HttpServletRequest req, HttpServletResponse resp) {
        return userDatabase.findAll();
    }

    public void update(HttpServletRequest req, HttpServletResponse resp) {
        String id = req.getRequestURI().split("/")[2];
        User user = userDatabase.find(id);
        if (user == null) throw new IllegalArgumentException("User not found!");

        validatePassword(req, user);
        String password = req.getParameter("password");
        String name = req.getParameter("name");
        String email = req.getParameter("email");

        userDatabase.update(id, User.of(user.getId(), password, name, email));
        System.out.println(userDatabase.findAll());
    }

    private void validatePassword(HttpServletRequest req, User user) {
        String beforePassword = req.getParameter("before-password");
        if (!user.getPassword().equals(beforePassword)) {
            throw new IllegalArgumentException("Password is incorrect!");
        }
    }
}
