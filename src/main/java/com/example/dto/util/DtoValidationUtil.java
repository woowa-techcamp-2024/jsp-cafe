package com.example.dto.util;

import java.lang.reflect.Field;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.annotation.NotNull;
import com.example.exception.BaseException;

public class DtoValidationUtil {

	private static final Logger log = LoggerFactory.getLogger(DtoValidationUtil.class);

	private DtoValidationUtil() {
	}

	public static void validate(Object obj) {
		Field[] fields = obj.getClass().getDeclaredFields();
		for (Field field : fields) {
			if (field.isAnnotationPresent(NotNull.class)) {
				field.setAccessible(true);
				try {
					if (field.get(obj) == null || ((String)field.get(obj)).isBlank()) {
						throw BaseException.exception(400, "field is null or blank");
					}
				} catch (IllegalAccessException e) {
					throw BaseException.serverException();
				}
			}
		}
	}
}
