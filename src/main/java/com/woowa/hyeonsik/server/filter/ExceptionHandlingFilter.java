package com.woowa.hyeonsik.server.filter;

import com.woowa.hyeonsik.application.exception.AuthenticationException;
import com.woowa.hyeonsik.application.exception.AuthorizationException;
import com.woowa.hyeonsik.application.exception.LoginFailedException;
import com.woowa.hyeonsik.application.exception.LoginRequiredException;
import com.woowa.hyeonsik.application.util.SendPageUtil;
import com.woowa.hyeonsik.server.database.JdbcException;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 모든 경로에 대하여, 요청을 처리하는 중에 발생하는 예외를 catch하고 적절한 응답을 반환하는 필터
 */
@WebFilter("/*")
public class ExceptionHandlingFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandlingFilter.class);

    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain filterChain)
        throws IOException, ServletException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        // filterChain 처리 중 전파되는 Exception을 Catch하고 처리한다.
        try {
            filterChain.doFilter(request, response);
        } catch(LoginFailedException e) {
            logger.debug("인증 실패 - 인증 정보가 일치하지 않습니다.");
            SendPageUtil.redirect("/auth/login_failed.jsp", httpRequest.getServletContext(), httpResponse);
        } catch (LoginRequiredException e) {
            logger.debug("인증 실패 - 세션이 비어있습니다.");
            SendPageUtil.redirect("/auth/login_required.jsp", httpRequest.getServletContext(), httpResponse);
        } catch (AuthenticationException e) {
            logger.debug("인증 실패 - 정의되지 않은 인증 실패 케이스입니다.");
            SendPageUtil.redirect("/error/error.jsp", httpRequest.getServletContext(), httpResponse);
        } catch (AuthorizationException e) {
            logger.debug("인가 실패 - 접근 권한이 없음");
            SendPageUtil.redirect("/auth/login.jsp", httpRequest.getServletContext(), httpResponse);
        } catch (IllegalArgumentException e) {
            logger.debug("잘못된 요청입니다. 내용: {}", e.getMessage());
            request.setAttribute("error_message", e.getMessage());
            httpResponse.sendError(400, e.getMessage());
        } catch (JdbcException e) {
            logger.debug("데이터베이스 접근 중 에러가 발생했습니다. 내용: {}", e.getMessage());
            e.printStackTrace();
            SendPageUtil.redirect("/error/error.jsp", httpRequest.getServletContext(), httpResponse);
        } catch (Throwable t) {
            // 처리할 수 없는 예외는 로그만 남기고 throw 한다.
            t.printStackTrace();
            throw t;
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
