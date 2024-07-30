package com.example.filter;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.exception.BaseException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebFilter(filterName = "ExceptionFilter", urlPatterns = "/*")
public class ExceptionFilter implements Filter {

	private final Logger log = LoggerFactory.getLogger(ExceptionFilter.class);

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws
		IOException,
		ServletException {
		try {
			chain.doFilter(request, response);
		} catch (Exception e) {
			if (e instanceof BaseException) {
				handleBaseException((BaseException)e, (HttpServletRequest)request, (HttpServletResponse)response);
			} else {
				handleGenericException(e, (HttpServletRequest)request, (HttpServletResponse)response);
			}
		}
	}

	private void handleBaseException(BaseException e, HttpServletRequest request, HttpServletResponse response)
		throws IOException, ServletException {
		request.setAttribute("statusCode", e.getStatus());
		request.setAttribute("message", e.getMessage());
		response.setStatus(e.getStatus());
		request.getRequestDispatcher("/WEB-INF/error.jsp").forward(request, response);
		// response.setStatus(e.getStatus());
		// response.sendRedirect("/error.jsp");
	}

	private void handleGenericException(Exception e, HttpServletRequest request, HttpServletResponse response)
		throws IOException, ServletException {
		log.error(e.getMessage());
		request.setAttribute("statusCode", 500);
		request.setAttribute("message", "Internal Server Error");
		response.setStatus(500);
		request.getRequestDispatcher("/WEB-INF/error.jsp").forward(request, response);
		// response.sendRedirect("/error.jsp");
	}
}
