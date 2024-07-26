package com.codesquad.cafe.servlet;

import com.codesquad.cafe.db.InMemoryUserRepository;
import jakarta.servlet.ServletException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserLoginServlet extends UserServlet {

    private static final Logger log = LoggerFactory.getLogger(UserLoginServlet.class);

    private InMemoryUserRepository userRepository;

    @Override
    public void init() throws ServletException {
        super.init();
        this.userRepository = (InMemoryUserRepository) getServletContext().getAttribute("userRepository");
    }

//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        String username = req.getParameter("username");
//        String password = req.getParameter("password");
//        req.login(username, password);
//        if(!req.authenticate(resp)){
//            resp.sendError(401);
//            return;
//        }
//        resp.setHeader("Set-Cookie", req.getSession(false).getId());
//        resp.p
//        resp.sendRedirect("/");
//        super.doPost(req, resp);
//    }
}
