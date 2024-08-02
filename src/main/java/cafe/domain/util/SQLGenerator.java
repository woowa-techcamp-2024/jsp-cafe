package cafe.domain.util;

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
        if (className == null || fields == null) {
            throw new IllegalArgumentException("Invalid arguments");
        }

        StringBuilder stringBuilder = new StringBuilder("INSERT INTO `").append(className).append("s` (");
        for (Field field : fields) {
            if (field.getName().equals("this$0")) continue;
            stringBuilder.append("`").append(field.getName()).append("`, ");
        }
        stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());
        stringBuilder.append(") VALUES (");
        for (Field field : fields) {
            if (field.getName().equals("this$0")) continue;
            stringBuilder.append("?, ");
        }
        stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());
        stringBuilder.append(");");
        return stringBuilder.toString();
    }

    public String generateSelectByIdSQL(String className) {
        if (className == null) {
            throw new IllegalArgumentException("Invalid arguments");
        }
        return "SELECT * FROM `" + className + "s` WHERE `" + className + "Id` = ?;";
    }

    public String generateSelectAllSQL(String className) {
        if (className == null) {
            throw new IllegalArgumentException("Invalid arguments");
        }
        return "SELECT * FROM `" + className + "s` ORDER BY `created` ASC;";
    }

    public String generateUpdateSQL(String className, Field[] fields) {
        if (className == null || fields == null) {
            throw new IllegalArgumentException("Invalid arguments");
        }

        StringBuilder stringBuilder = new StringBuilder("UPDATE `").append(className).append("s` SET ");
        for (Field field : fields) {
            if (field.getName().equals("this$0")) continue;
            stringBuilder.append("`").append(field.getName()).append("` = ?, ");
        }
        stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());
        stringBuilder.append(" WHERE `").append(className).append("Id` = ?;");
        return stringBuilder.toString();
    }

    public String generateDeleteAllSQL(String className) {
        if (className == null) {
            throw new IllegalArgumentException("Invalid arguments");
        }
        return "DELETE FROM `" + className + "s`;";
    }

    public <K> String generateDeleteByIdSQL(String className) {
        if (className == null) {
            throw new IllegalArgumentException("Invalid arguments");
        }
        return "UPDATE `" + className + "s` SET `deleted`=true WHERE `" + className + "Id` = ?;";
    }

    public String generateDeleteHardAllSQL(String className) {
        if (className == null) {
            throw new IllegalArgumentException("Invalid arguments");
        }
        return "DELETE FROM `" + className + "s`;";
    }
}
