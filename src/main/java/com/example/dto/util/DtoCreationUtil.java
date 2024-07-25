package com.example.dto.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.http.HttpServletRequest;

public class DtoCreationUtil {

	private static final Logger log = LoggerFactory.getLogger(DtoCreationUtil.class);

	private DtoCreationUtil() {
	}

	public static <T> T createDto(Class<T> clazz, HttpServletRequest req) {
		Field[] fields = clazz.getDeclaredFields();
		Constructor<?> constructor = clazz.getDeclaredConstructors()[0];
		List<Object> params = new ArrayList<>();
		for (Field field : fields) {
			field.setAccessible(true);
			if (Modifier.isStatic(field.getModifiers())) {
				continue;
			}
			params.add(req.getParameter(field.getName()));
		}
		try {
			return (T)constructor.newInstance(params.toArray());
		} catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
			log.error(e.getMessage());
			throw new RuntimeException(e);
		}
	}
}
