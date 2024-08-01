package com.example.filter;

import java.io.IOException;
import java.lang.reflect.Method;

import com.example.annotation.Login;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@WebFilter("/*")
public class LoginFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		Filter.super.init(filterConfig);
		ServletMapper.init(filterConfig.getServletContext());
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
		throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest)request;
		HttpSession session = httpRequest.getSession(false);

		String servletPath = httpRequest.getServletPath();
		String httpMethod = "do" + httpRequest.getMethod().substring(0, 1).toUpperCase() + httpRequest.getMethod()
			.substring(1).toLowerCase();

		try {
			String className = ServletMapper.getServletClassName(servletPath);

			if (className != null) {
				Class<?> clazz = Class.forName(className);
				Method[] methods = clazz.getDeclaredMethods();

				for (Method method : methods) {
					if (method.isAnnotationPresent(Login.class) && method.getName().equalsIgnoreCase(httpMethod)) {
						if (session == null || session.getAttribute("login") == null) {
							httpRequest.getRequestDispatcher("/users/login").forward(request, response);
							return;
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		chain.doFilter(request, response);
	}
}
