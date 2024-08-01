package woopaca.jspcafe.database;

import woopaca.jspcafe.model.ContentStatus;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class RowMapper {

    private RowMapper() {
    }

    public static <T> T mapRow(ResultSet resultSet, Class<T> clazz) throws SQLException {
        try {
            Object[] arguments = extractFieldValues(resultSet, clazz);
            Constructor<T> constructor = findSuitableConstructor(clazz, arguments);
            return constructor.newInstance(arguments);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> List<T> mapRows(ResultSet resultSet, Class<T> clazz) throws SQLException {
        List<T> results = new ArrayList<>();
        while (resultSet.next()) {
            T mapped = mapRow(resultSet, clazz);
            results.add(mapped);
        }
        return results;
    }

    private static <T> Constructor<T> findSuitableConstructor(Class<T> clazz, Object[] arguments) throws NoSuchMethodException {
        return clazz.getDeclaredConstructor(
                Arrays.stream(arguments)
                        .map(Object::getClass)
                        .toArray(Class[]::new)
        );
    }

    private static Object[] extractFieldValues(ResultSet resultSet, Class<?> clazz) throws SQLException, IllegalArgumentException {
        Field[] fields = clazz.getDeclaredFields();
        Object[] values = new Object[fields.length];
        for (int i = 0; i < fields.length; i++) {
            String fieldName = fields[i].getName();
            Class<?> fieldType = fields[i].getType();
            String snakeCaseFieldName = replaceSnakeCase(fieldName);
            values[i] = getValueFromResultSet(resultSet, snakeCaseFieldName, fieldType);
        }
        return values;
    }

    private static String replaceSnakeCase(String fieldName) {
        return fieldName.replaceAll("([a-z])([A-Z]+)", "$1_$2")
                .toLowerCase();
    }

    private static Object getValueFromResultSet(ResultSet resultSet, String fieldName, Class<?> fieldType) throws SQLException {
        return switch (fieldType.getName()) {
            case "int", "java.lang.Integer" -> resultSet.getInt(fieldName);
            case "long", "java.lang.Long" -> resultSet.getLong(fieldName);
            case "double", "java.lang.Double" -> resultSet.getDouble(fieldName);
            case "boolean", "java.lang.Boolean" -> resultSet.getBoolean(fieldName);
            case "java.lang.String" -> resultSet.getString(fieldName);
            case "java.time.LocalDate" -> {
                Timestamp fieldValue = resultSet.getTimestamp(fieldName);
                yield fieldValue.toLocalDateTime().toLocalDate();
            }
            case "java.time.LocalDateTime" -> {
                Timestamp fieldValue = resultSet.getTimestamp(fieldName);
                yield fieldValue.toLocalDateTime();
            }
            case "woopaca.jspcafe.model.ContentStatus" -> ContentStatus.valueOf(resultSet.getString(fieldName));
            default -> resultSet.getObject(fieldName);
        };
    }
}
