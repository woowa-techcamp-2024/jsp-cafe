package cafe.domain.sql;

import java.lang.reflect.Field;

public class SQLGenerator {
    private static SQLGenerator sqlGenerator;

    private SQLGenerator() { }

    public static SQLGenerator getInstance() {
        if (sqlGenerator == null) {
            sqlGenerator = new SQLGenerator();
        }
        return sqlGenerator;
    }

    public String generateInsertSQL(String className, Field[] fields) {
        StringBuilder stringBuilder = new StringBuilder("INSERT INTO `").append(className).append("s` (");
        stringBuilder.append("`id`, ");
        for (Field field : fields) {
            stringBuilder.append("`").append(field.getName()).append("`, ");
        }
        stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());
        stringBuilder.append(") VALUES (?, ");
        for (Field field : fields) {
            stringBuilder.append("?, ");
        }
        stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());
        stringBuilder.append(");");
        return stringBuilder.toString();
    }

    public String generateSelectByIdSQL(String className) {
        return "SELECT * FROM `" + className + "s` WHERE `id` = ?;";
    }

    public String generateSelectAllSQL(String className) {
        return "SELECT * FROM `" + className + "s`;";
    }

    public String generateUpdateSQL(String className, Field[] fields) {
        StringBuilder stringBuilder = new StringBuilder("UPDATE `").append(className).append("s` SET ");
        for (Field field : fields) {
            stringBuilder.append("`").append(field.getName()).append("` = ?, ");
        }
        stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());
        stringBuilder.append(" WHERE `id` = ?;");
        return stringBuilder.toString();
    }
}
