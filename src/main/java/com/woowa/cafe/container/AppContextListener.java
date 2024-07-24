package com.woowa.cafe.container;

import com.woowa.cafe.repository.qna.ArticleRepository;
import com.woowa.cafe.repository.qna.InMemoryArticleRepository;
import com.woowa.cafe.repository.member.InMemoryMemberRepository;
import com.woowa.cafe.repository.member.MemberRepository;
import com.woowa.cafe.service.ArticleService;
import com.woowa.cafe.service.MemberService;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class AppContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(final ServletContextEvent sce) {
        registryMemberService(sce);
        registryArticleService(sce);
    }

    private void registryMemberService(final ServletContextEvent sce) {
        MemberRepository memberRepository = new InMemoryMemberRepository();
        MemberService memberService = new MemberService(memberRepository);

        sce.getServletContext().setAttribute("memberService", memberService);
        sce.getServletContext().setAttribute("memberRepository", memberRepository);
    }

    private void registryArticleService(final ServletContextEvent sce) {
        ArticleRepository articleRepository = new InMemoryArticleRepository();
        MemberRepository memberRepository = (MemberRepository) sce.getServletContext().getAttribute("memberRepository");
        ArticleService articleService = new ArticleService(articleRepository, memberRepository);

        sce.getServletContext().setAttribute("articleService", articleService);
        sce.getServletContext().setAttribute("articleRepository", articleRepository);
    }

}
