package codesquad.javacafe.member.controller;

import codesquad.javacafe.common.SubController;
import codesquad.javacafe.member.dto.request.MemberCreateRequestDto;
import codesquad.javacafe.member.dto.response.MemberResponseDto;
import codesquad.javacafe.member.entity.Member;
import codesquad.javacafe.member.repository.MemberRepository;
import codesquad.javacafe.member.service.MemberService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;


public class MemberController implements SubController {
    private static final Logger log = LoggerFactory.getLogger(MemberController.class);
    private static final MemberService memberService = MemberService.getInstance();

    @Override
    public void doProcess(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        log.info("[MemberController doProcess]");
        var method = req.getMethod();
        log.info("[MemberController doProcess] method: {}", method);
        switch (method) {
            case "GET" :{
                var memberList = getMemberList();
                for (MemberResponseDto memberResponseDto : memberList) {
                    System.out.println(memberResponseDto);
                }
                req.setAttribute("memberList", memberList);
                var dispatcher = req.getRequestDispatcher("/WEB-INF/user/list.jsp");
                dispatcher.forward(req,res);
            }
            case "POST" : {
                createMember(req,res);
                res.sendRedirect("/api/users");
            }
        }
    }

    private List<MemberResponseDto> getMemberList() {
        return memberService.getMemberList();
    }

    private void createMember(HttpServletRequest req, HttpServletResponse res) {
        var body = req.getParameterMap();
        var memberDto = new MemberCreateRequestDto(body);
        memberService.createMember(memberDto);
    }
}
