package com.woowa.cafe.container;

import com.woowa.cafe.repository.user.InMemoryMemberRepository;
import com.woowa.cafe.repository.user.MemberRepository;
import com.woowa.cafe.service.MemberService;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class AppContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(final ServletContextEvent sce) {
        registryMemberService(sce);
    }

    private void registryMemberService(final ServletContextEvent sce) {
        MemberRepository memberRepository = new InMemoryMemberRepository();
        MemberService memberService = new MemberService(memberRepository);
        sce.getServletContext().setAttribute("memberService", memberService);
        sce.getServletContext().setAttribute("memberRepository", memberRepository);
    }

}
