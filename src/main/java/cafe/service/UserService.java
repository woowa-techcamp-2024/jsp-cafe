package cafe.service;

import cafe.domain.db.UserDatabase;
import cafe.domain.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

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

        userDatabase.save(new User(id, password, name, email));
    }

    public User find(HttpServletRequest req, HttpServletResponse resp) {
        String uri = req.getRequestURI();
        String id = uri.substring(uri.lastIndexOf("/") + 1);

        User user = userDatabase.find(id);
        if (user == null) throw new IllegalArgumentException("User not found!");
        return user;
    }

    public List<User> findAll(HttpServletRequest req, HttpServletResponse resp) {
        return List.copyOf(userDatabase.findAll().values());
    }
}
