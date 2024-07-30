package cafe.domain.db;

import cafe.domain.util.DatabaseConnector;
import cafe.domain.util.SQLExecutor;
import cafe.domain.util.SQLGenerator;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public interface Database<K, V> {
    SQLGenerator sqlGenerator = SQLGenerator.getInstance();
    SQLExecutor sqlExecutor = SQLExecutor.getInstance();

    default DatabaseConnector getConnector() {
        return null;
    }

    default void insert(V data) {
        String className = findClassName();
        Field[] fields = findFields();
        for (Field field : fields) {
            field.setAccessible(true);
        }

        try (Connection connection = getConnector().connect()) {
            String insertSQL = sqlGenerator.generateInsertSQL(className, fields);
            sqlExecutor.executeInsert(connection, insertSQL, fields, data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    default V selectById(K id) {
        String className = findClassName();
        Field[] fields = findFields();
        Constructor<V> constructor = (Constructor<V>) findConstructor();
        constructor.setAccessible(true);

        V data = null;
        try (Connection connection = getConnector().connect()) {
            String selectSQL = sqlGenerator.generateSelectByIdSQL(className);
            ResultSet rs = sqlExecutor.executeSelectById(connection, selectSQL, id);

            if (!rs.next()) return null;
            Object[] objects = new Object[fields.length];
            for (int i = 0; i < fields.length; i++) {
                objects[i] = rs.getString(fields[i].getName());
            }
            data = constructor.newInstance(objects);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    default Map<K, V> selectAll() {
        String className = findClassName();
        Field[] fields = findFields();
        Constructor<V> constructor = (Constructor<V>) findConstructor();
        constructor.setAccessible(true);

        Map<K, V> result = new HashMap<>();
        try (Connection connection = getConnector().connect()) {
            String selectAllSQL = sqlGenerator.generateSelectAllSQL(className);
            ResultSet rs = sqlExecutor.executeSelectAll(connection, selectAllSQL);

            while (rs.next()) {
                Object[] objects = new Object[fields.length];
                for (int i = 0; i < fields.length; i++) objects[i] = rs.getString(fields[i].getName());
                V data = constructor.newInstance(objects);
                result.put((K) new String(rs.getBytes(1)), data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    default void update(K id, V data) {
        String className = findClassName();
        Field[] fields = findFields();
        for (Field field : fields) field.setAccessible(true);

        try (Connection connection = getConnector().connect()) {
            String updateSQL = sqlGenerator.generateUpdateSQL(className, fields);
            sqlExecutor.executeUpdate(connection, updateSQL, fields, data, id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    default void deleteById(K id) {}
    default void deleteAll() {
        String className = findClassName();
        try (Connection connection = getConnector().connect()) {
            String deleteAllSQL = sqlGenerator.generateDeleteAllSQL(className);
            connection.prepareStatement(deleteAllSQL).executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Field[] findFields() {
        Class<?> clazz = (Class<?>) ((ParameterizedType) getClass().getGenericInterfaces()[0]).getActualTypeArguments()[1];
        return clazz.getDeclaredFields();
    }

    private String findClassName() {
        ParameterizedType type = (ParameterizedType) getClass().getGenericInterfaces()[0];
        Class<?> clazz = (Class<?>) type.getActualTypeArguments()[1];
        return clazz.getSimpleName().toLowerCase();
    }

    private Constructor<?> findConstructor() {
        try {
            Class<?> clazz = (Class<?>) ((ParameterizedType) getClass().getGenericInterfaces()[0]).getActualTypeArguments()[1];
            return clazz.getDeclaredConstructors()[0];
        } catch (Exception e) {
            return null;
        }
    }

}
