package com.woowa.cafe.container;

import com.woowa.cafe.config.DataSourceConfig;
import com.woowa.cafe.repository.member.JdbcMemberRepository;
import com.woowa.cafe.repository.member.MemberRepository;
import com.woowa.cafe.repository.qna.ArticleRepository;
import com.woowa.cafe.repository.qna.JdbcArticleRepository;
import com.woowa.cafe.repository.qna.JdbcReplyRepository;
import com.woowa.cafe.repository.qna.ReplyRepository;
import com.woowa.cafe.service.ArticleService;
import com.woowa.cafe.service.MemberService;
import com.woowa.cafe.service.ReplyService;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import javax.sql.DataSource;

@WebListener
public class AppContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(final ServletContextEvent sce) {
        DataSource dataSource = getDataSource(sce);
        registryMemberService(sce, dataSource);
        registryArticleService(sce, dataSource);
    }

    private static DataSource getDataSource(final ServletContextEvent sce) {
        DataSourceConfig dataSourceConfig = new DataSourceConfig(sce.getServletContext());
        DataSource dataSource = dataSourceConfig.getDataSource();

        sce.getServletContext().setAttribute("dataSource", dataSource);

        return dataSource;
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
        ReplyRepository replyRepository = new JdbcReplyRepository(dataSource);

        ArticleService articleService = new ArticleService(articleRepository, memberRepository, replyRepository);

        sce.getServletContext().setAttribute("articleService", articleService);
        sce.getServletContext().setAttribute("articleRepository", articleRepository);

        ReplyService replyService = new ReplyService(articleRepository, replyRepository);

        sce.getServletContext().setAttribute("replyService", replyService);
        sce.getServletContext().setAttribute("replyRepository", replyRepository);
    }

    @Override
    public void contextDestroyed(final ServletContextEvent sce) {
        DataSource dataSource = (DataSource) sce.getServletContext().getAttribute("dataSource");
        if (dataSource instanceof HikariDataSource) {
            ((HikariDataSource) dataSource).close();
        }
    }
}
