package lass9436.utils;

import java.io.IOException;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class pageUtil {

	public static void showPage(Map<String, String> actionPageMap, String path, HttpServletRequest req, HttpServletResponse resp) throws
		IOException,
		ServletException {
		String action = req.getParameter("action");
		if (action == null) {
			resp.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		String page = actionPageMap.get(action);
		if (page == null) {
			resp.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		req.getRequestDispatcher(path + page).forward(req, resp);
	}
}
