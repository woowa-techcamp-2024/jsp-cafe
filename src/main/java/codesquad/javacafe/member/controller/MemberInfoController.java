package codesquad.javacafe.member.controller;

import codesquad.javacafe.common.SubController;
import codesquad.javacafe.member.dto.response.MemberResponseDto;
import codesquad.javacafe.member.service.MemberService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class MemberInfoController implements SubController {
    private static final Logger log = LoggerFactory.getLogger(MemberInfoController.class);
    private static final MemberService memberService = MemberService.getInstance();

    @Override
    public void doProcess(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        log.info("[MemberInfoController doProcess]");
        var method = req.getMethod();
        log.info("[MemberInfoController doProcess] method: {}", method);

        switch (method) {
            case "GET":{
                var userId = req.getParameter("userId");
                var memberInfo = memberService.getMemberInfo(userId);
                req.setAttribute("memberInfo", memberInfo);
                var dispatcher = req.getRequestDispatcher("/WEB-INF/user/profile.jsp");
                dispatcher.forward(req, res);
            }
        }
    }
}
