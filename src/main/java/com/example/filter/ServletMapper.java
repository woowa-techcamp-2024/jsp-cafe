package com.example.filter;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletRegistration;

public class ServletMapper {

	private static final Map<Pattern, String> patternServletMap = new HashMap<>();

	public static void init(ServletContext context) {
		Map<String, ? extends ServletRegistration> registrations = context.getServletRegistrations();

		for (ServletRegistration registration : registrations.values()) {
			for (String mapping : registration.getMappings()) {
				String regex = mapping
					.replace(".", "\\.")
					.replace("/*", "(/.*)?")
					.replace("*", ".*");
				Pattern pattern = Pattern.compile(regex);
				patternServletMap.put(pattern, registration.getClassName());
			}
		}
	}

	public static String getServletClassName(String servletPath) {
		for (Map.Entry<Pattern, String> entry : patternServletMap.entrySet()) {
			if (entry.getKey().matcher(servletPath).matches()) {
				return entry.getValue();
			}
		}
		return null;
	}
}
