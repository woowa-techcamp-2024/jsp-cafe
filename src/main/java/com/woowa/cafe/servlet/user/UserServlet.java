package com.woowa.cafe.servlet.user;

import com.woowa.cafe.dto.member.SaveMemberDto;
import com.woowa.cafe.dto.member.UpdateMemberDto;
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

import static org.slf4j.LoggerFactory.getLogger;

@WebServlet(name = "userServlet", value = {"/user", "/user/*"})
public class UserServlet extends HttpServlet {

    private static final Logger log = getLogger(UserServlet.class);
    public static final String SESSION_INFO = "memberId";

    private MemberService memberService;

    @Override
    public void init() throws ServletException {
        this.memberService = (MemberService) getServletContext().getAttribute("memberService");
        log.info("UserServlet is initialized");
    }

    @Override
    public void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        String requestURI = req.getRequestURI();
        log.info("requestURI: {}", requestURI);

        if (requestURI.endsWith("/user")) {
            getForm(req, resp);
            return;
        }

        if (req.getPathInfo().endsWith("/login")) {
            getLoginForm(req, resp);
            return;
        }

        String[] path = req.getPathInfo().split("/");
        String lastPath = path[path.length - 1];

        if (lastPath.equals("form")) {
            getUpdateForm(req, resp, path[path.length - 2]);
        }

        getProfile(req, resp, lastPath);
    }

    private void getLoginForm(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/user/login.jsp").forward(req, resp);
    }

    private void getForm(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/user/form.jsp").forward(req, resp);
    }

    private void getProfile(final HttpServletRequest req, final HttpServletResponse resp, final String userId) throws ServletException, IOException {
        if (req.getSession().getAttribute("memberId") == null || !req.getSession().getAttribute("memberId").equals(userId)) {
            resp.sendRedirect("/users");
            return;
        }

        req.setAttribute("member", memberService.findById(userId));
        req.getRequestDispatcher("/WEB-INF/views/user/profile.jsp").forward(req, resp);
    }

    private void getUpdateForm(final HttpServletRequest req, final HttpServletResponse resp, final String userId) throws ServletException, IOException {
        req.setAttribute("member", memberService.findById(userId));
        req.getRequestDispatcher("/WEB-INF/views/user/updateForm.jsp").forward(req, resp);
    }

    @Override
    public void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        if (req.getRequestURI().endsWith("/user")) {
            saveMember(req, resp);
            return;
        }

        if (req.getPathInfo().endsWith("/login")) {
            login(req, resp);
            return;
        }

        if (req.getPathInfo().endsWith("/logout")) {
            logout(req, resp);
            return;
        }


        String pathInfo = req.getPathInfo();
        String[] path = pathInfo.split("/");
        String memberId = path[path.length - 1];

        updateMember(req, resp, memberId);
    }

    private void login(final HttpServletRequest req, final HttpServletResponse resp) throws IOException, ServletException {
        Map<String, String> body = HttpMessageUtils.getBodyFormData(req);

        try {
            String loginId = memberService.login(body.get("userId").trim(), body.get("password").trim());
            HttpSession session = req.getSession();
            session.setAttribute(SESSION_INFO, loginId);

            resp.sendRedirect("/users");
        } catch (IllegalArgumentException e) {
            req.setAttribute("loginFailed", true);
            req.getRequestDispatcher("/WEB-INF/views/user/login.jsp").forward(req, resp);
            return;
        }
    }

    private void logout(final HttpServletRequest req, final HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        if (session != null || session.getAttribute(SESSION_INFO) != null) {
            session.invalidate();
        }

        resp.sendRedirect("/");
    }

    private void saveMember(final HttpServletRequest req, final HttpServletResponse resp) throws IOException {
        Map<String, String> body = HttpMessageUtils.getBodyFormData(req);

        String save = memberService.save(SaveMemberDto.from(body));

        HttpSession session = req.getSession();
        session.setAttribute(SESSION_INFO, save);

        resp.sendRedirect("/users");
    }

    private void updateMember(final HttpServletRequest req, final HttpServletResponse resp, final String memberId) throws IOException {
        Map<String, String> body = HttpMessageUtils.getBodyFormData(req);

        HttpSession session = req.getSession();
        String loginId = (String) session.getAttribute(SESSION_INFO);

        // 로그인 페이지로 이동 처리
        if (loginId == null || !loginId.equals(memberId)) {
            resp.sendRedirect("/users");
            return;
        }

        memberService.update(memberId, UpdateMemberDto.from(body));

        resp.sendRedirect("/user/" + memberId);
    }


}
