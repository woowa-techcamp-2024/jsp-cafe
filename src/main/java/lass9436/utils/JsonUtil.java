package lass9436.utils;

import java.io.BufferedReader;
import java.io.IOException;

import org.json.JSONObject;

import jakarta.servlet.http.HttpServletRequest;

public class JsonUtil {
	public static JSONObject parseJson(HttpServletRequest req) throws IOException {
		StringBuilder sb = new StringBuilder();
		try (BufferedReader reader = req.getReader()) {
			String line;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		}
		return new JSONObject(sb.toString());
	}
}
