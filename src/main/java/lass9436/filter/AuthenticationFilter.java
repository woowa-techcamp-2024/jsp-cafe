package lass9436.filter;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebFilter({"/questions", "/questionPage"})
public class AuthenticationFilter implements Filter {

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws
		IOException,
		ServletException {
		HttpServletRequest req = (HttpServletRequest) servletRequest;
		HttpServletResponse resp = (HttpServletResponse) servletResponse;
		String fullUri = req.getRequestURI() + "?" + req.getQueryString();
		try {
			// 글 목록 페이지는 로그인을 하지 않아도 보여줌
			if ("/questionPage?action=list".equals(fullUri)) {
				filterChain.doFilter(req, resp);
				return;
			}

			// 그 외의 페이지는 로그인하지 않으면 볼 수 없음
			long userSeq = (long) req.getSession().getAttribute("userSeq");
			filterChain.doFilter(req, resp);
		} catch (RuntimeException e) {
			resp.sendRedirect("/userPage?action=login");
		}
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		Filter.super.init(filterConfig);
	}

	@Override
	public void destroy() {
		Filter.super.destroy();
	}
}
