package com.woowa.cafe.container;

import com.woowa.cafe.config.DataSourceConfig;
import com.woowa.cafe.repository.member.JdbcMemberRepository;
import com.woowa.cafe.repository.qna.ArticleRepository;
import com.woowa.cafe.repository.qna.InMemoryArticleRepository;
import com.woowa.cafe.repository.member.InMemoryMemberRepository;
import com.woowa.cafe.repository.member.MemberRepository;
import com.woowa.cafe.repository.qna.JdbcArticleRepository;
import com.woowa.cafe.service.ArticleService;
import com.woowa.cafe.service.MemberService;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import javax.sql.DataSource;

@WebListener
public class AppContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(final ServletContextEvent sce) {
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        DataSource dataSource = dataSourceConfig.getDataSource();
        registryMemberService(sce, dataSource);
        registryArticleService(sce, dataSource);
    }

    private void registryMemberService(final ServletContextEvent sce, final DataSource dataSource) {
        MemberRepository memberRepository = new JdbcMemberRepository(dataSource);
        MemberService memberService = new MemberService(memberRepository);

        sce.getServletContext().setAttribute("memberService", memberService);
        sce.getServletContext().setAttribute("memberRepository", memberRepository);
    }

    private void registryArticleService(final ServletContextEvent sce, final DataSource dataSource) {
        ArticleRepository articleRepository = new JdbcArticleRepository(dataSource);
        MemberRepository memberRepository = (MemberRepository) sce.getServletContext().getAttribute("memberRepository");
        ArticleService articleService = new ArticleService(articleRepository, memberRepository);

        sce.getServletContext().setAttribute("articleService", articleService);
        sce.getServletContext().setAttribute("articleRepository", articleRepository);
    }

}
