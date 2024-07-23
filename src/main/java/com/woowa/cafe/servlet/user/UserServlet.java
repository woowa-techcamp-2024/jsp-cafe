package com.woowa.cafe.servlet.user;

import com.woowa.cafe.dto.SaveMemberDto;
import com.woowa.cafe.service.MemberService;
import com.woowa.cafe.utils.HttpMessageUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import static org.slf4j.LoggerFactory.getLogger;

@WebServlet(name = "userServlet", value = {"/user", "/user/*"})
public class UserServlet extends HttpServlet {

    private static final Logger log = getLogger(UserServlet.class);

    private MemberService memberService;

    @Override
    public void init() throws ServletException {
        this.memberService = (MemberService) getServletContext().getAttribute("memberService");
        log.info("UserServlet is initialized");
    }

    @Override
    public void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        if(req.getRequestURI().endsWith("/user")) {
            getForm(req, resp);
            return;
        }
    }

    private void getForm(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("WEB-INF/views/user/form.jsp").forward(req, resp);
    }

    @Override
    public void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        saveMember(req, resp);
    }

    private void saveMember(final HttpServletRequest req, final HttpServletResponse resp) throws IOException {
        Map<String, String> body = HttpMessageUtils.getBodyFormData(req);

        String save = memberService.save(SaveMemberDto.from(body));

        HttpSession session = req.getSession();
        session.setAttribute(UUID.randomUUID().toString(), save);

        resp.sendRedirect("/users");
    }

}
