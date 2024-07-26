package woopaca.jspcafe.database;

public final class DatabaseInitializer {

    public static void initialize(JdbcTemplate jdbcTemplate) {
        jdbcTemplate.update(MySQLConstants.CREATE_USER_TABLE);
        jdbcTemplate.update(MySQLConstants.CREATE_POST_TABLE);
    }
}
