package com.woowa.cafe.servlet.user;

import com.woowa.cafe.domain.Member;
import com.woowa.cafe.service.MemberService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@WebServlet(name = "usersServlet", value = "/users")
public class UsersServlet extends HttpServlet {

    private static final Logger log = getLogger(UsersServlet.class);

    private MemberService memberService;

    @Override
    public void init() throws ServletException {
        this.memberService = (MemberService) getServletContext().getAttribute("memberService");
        log.info("UsersServlet is initialized");
    }

    @Override
    public void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        getUserList(req, resp);
    }

    private void getUserList(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        List<Member> members = memberService.findAll();

        req.setAttribute("members", members);
        req.getRequestDispatcher("/WEB-INF/views/user/list.jsp").forward(req, resp);
    }
}
