package com.codesquad.cafe.util;

import java.io.BufferedReader;
import java.io.IOException;

import com.codesquad.cafe.exception.ModelMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public final class JsonModelMapper {

	private static final ObjectMapper objectMapper = new ObjectMapper();

	private JsonModelMapper() {
	}

	public static <T> T map(HttpServletRequest request, Class<T> clazz) {
		StringBuilder jsonBuffer = new StringBuilder();
		try (BufferedReader reader = request.getReader()) {
			String line;
			while ((line = reader.readLine()) != null) {
				jsonBuffer.append(line);
			}

			// JSON을 MyData 객체로 변환합니다.
			String json = jsonBuffer.toString();
			return objectMapper.readValue(json, clazz);
		} catch (IOException e) {
			throw new ModelMappingException();
		}
	}

	public static <T> void json(HttpServletResponse response, T model) {
		try {
			response.setContentType("application/json;charset=utf-8");
			response.getWriter().write(objectMapper.writeValueAsString(model));
		} catch (IOException e) {
			throw new ModelMappingException();
		}
	}

}
