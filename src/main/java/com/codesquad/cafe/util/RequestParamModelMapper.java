package com.codesquad.cafe.util;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codesquad.cafe.exception.ModelMappingException;

public final class RequestParamModelMapper {

	private static final Logger log = LoggerFactory.getLogger(RequestParamModelMapper.class);

	private RequestParamModelMapper() {
	}

	public static <T> T map(Map<String, String[]> requestParameters, Class<T> type) {
		try {
			T model = type.getDeclaredConstructor().newInstance();

			for (Field field : type.getDeclaredFields()) {
				setField(requestParameters, field, model);
			}

			return model;
		} catch (Exception e) {
			log.warn("fail to map parameters to model {} : {} ", type.getName(), e.getMessage());
			throw new ModelMappingException("fail to map parameters to model " + type.getName());
		}
	}

	private static <T> boolean setField(Map<String, String[]> requestParameters, Field field, T model)
		throws IllegalAccessException {
		if (!requestParameters.containsKey(field.getName())) {
			return true;
		}
		String[] values = requestParameters.get(field.getName());

		if (!(List.class.isAssignableFrom(field.getType()) || values.length == 1)) {
			throw new IllegalArgumentException("Field type not supported: " + field.getType());
		}

		field.setAccessible(true);
		Object converted;
		if (List.class.isAssignableFrom(field.getType())) {
			converted = getList(field, values);
		} else {
			converted = getObject(field, values);
		}
		field.set(model, converted);
		field.setAccessible(false);
		return false;
	}

	private static List<Object> getList(Field field, String[] values) {
		Type genericType = ((ParameterizedType)field.getGenericType()).getActualTypeArguments()[0];
		List<Object> convertedValues = new ArrayList<>();
		for (String value : values) {
			Object convertedValue = castValue((Class<?>)genericType, value);
			convertedValues.add(convertedValue);
		}
		return convertedValues;
	}

	private static Object getObject(Field field, String[] values) {
		return castValue(field.getType(), values[0]);
	}

	private static Object castValue(Class<?> targetType, String value) throws IllegalArgumentException {
		if (targetType.equals(String.class)) {
			return value;
		} else if (targetType.equals(int.class) || targetType.equals(Integer.class)) {
			return Integer.parseInt(value);
		} else if (targetType.equals(double.class) || targetType.equals(Double.class)) {
			return Double.parseDouble(value);
		} else if (targetType.equals(boolean.class) || targetType.equals(Boolean.class)) {
			return Boolean.parseBoolean(value);
		} else if (targetType.equals(long.class) || targetType.equals(Long.class)) {
			return Long.parseLong(value);
		} else if (targetType.equals(float.class) || targetType.equals(Float.class)) {
			return Float.parseFloat(value);
		} else if (targetType.equals(short.class) || targetType.equals(Short.class)) {
			return Short.parseShort(value);
		} else if (targetType.equals(byte.class) || targetType.equals(Byte.class)) {
			return Byte.parseByte(value);
		} else if (targetType.equals(char.class) || targetType.equals(Character.class)) {
			if (value.length() != 1) {
				throw new IllegalArgumentException("String value cannot be converted to char: " + value);
			}
			return value.charAt(0);
		} else {
			throw new IllegalArgumentException("Unsupported target type: " + targetType);
		}
	}

}
