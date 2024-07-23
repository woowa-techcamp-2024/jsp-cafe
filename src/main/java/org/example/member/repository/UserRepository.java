package org.example.member.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.example.member.model.dao.User;
import org.example.member.model.dto.UserDto;
import org.example.util.DataUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserRepository {

    private static final Logger logger = LoggerFactory.getLogger(UserRepository.class);

    public UserDto register(User user) throws SQLException {
        String sql = "insert into user (userId, password, name, email) values (?, ?, ?, ?)";
        try (Connection conn = DataUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getUserId());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getName());
            ps.setString(4, user.getEmail());
            ps.executeUpdate();
            return UserDto.toDto(user);
        } catch (SQLException e) {
            logger.error("save user error", e);
            throw e;
        }
    }
}
