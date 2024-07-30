package codesquad.javacafe.member.controller;

import codesquad.javacafe.common.SubController;
import codesquad.javacafe.member.dto.request.MemberCreateRequestDto;
import codesquad.javacafe.member.dto.request.MemberUpdateRequestDto;
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

    @Override
    public void doProcess(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        log.info("[MemberInfoController doProcess]");
        var method = req.getMethod();
        log.info("[MemberInfoController doProcess] method: {}", method);
        switch(method){
            case "GET" : {
                var userId = req.getParameter("userId");
                req.setAttribute("userId", userId);
                var dispatcher = req.getRequestDispatcher("/user/memberInfo.jsp");
                dispatcher.forward(req, res);
                break;
            }
            case "POST" : {
                updateMember(req);
                var memberList = MemberService.getInstance().getMemberList();
                req.setAttribute("memberList", memberList);
                var dispatcher = req.getRequestDispatcher("/WEB-INF/user/list.jsp");
                dispatcher.forward(req, res);
                break;
            }
        }
    }

    private void updateMember(HttpServletRequest req) {
        var body = req.getParameterMap();
        var memberDto = new MemberUpdateRequestDto(body);
        MemberService.getInstance().updateMember(memberDto);
    }
}
