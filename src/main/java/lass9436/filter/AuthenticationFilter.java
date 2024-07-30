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
import jakarta.servlet.http.HttpSession;

@WebFilter({"/questions/*", "/questions", "/questionPage"})
public class AuthenticationFilter implements Filter {

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws
		IOException,
		ServletException {
		if (servletRequest instanceof HttpServletRequest req && servletResponse instanceof HttpServletResponse resp) {
			HttpSession session = req.getSession(false);
			if (session != null) {
				String userId = (String) session.getAttribute("userId");
				if (userId != null) {
					filterChain.doFilter(req, resp);
					return;
				}
				resp.sendRedirect("/userPage?action=login");
				return;
			}
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
