package lass9436.user.servlet;

import static lass9436.utils.pageUtil.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/userPage")
public class UserPageServlet extends HttpServlet {

	private Map<String, String> actionPageMap;
	private final String path = "/WEB-INF/user";

	@Override
	public void init(){
		actionPageMap = new HashMap<>();
		actionPageMap.put("register", "/register.jsp");
		actionPageMap.put("login-failed", "/loginFailed.jsp");
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		showPage(actionPageMap, path, req, resp);
	}
}
