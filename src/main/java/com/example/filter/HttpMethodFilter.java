package com.example.filter;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

@WebFilter(filterName = "HttpMethodFilter", urlPatterns = "/*")
public class HttpMethodFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
		throws IOException, ServletException {
		if (request instanceof HttpServletRequest) {
			HttpServletRequest httpRequest = (HttpServletRequest)request;
			String method = httpRequest.getParameter("_method");
			if (method != null && (method.equalsIgnoreCase("PUT") || method.equalsIgnoreCase("DELETE"))) {
				HttpServletRequestWrapper requestWrapper = new HttpServletRequestWrapper(httpRequest) {
					@Override
					public String getMethod() {
						return method;
					}
				};
				chain.doFilter(requestWrapper, response);
				return;
			}
		}
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
	}
}
