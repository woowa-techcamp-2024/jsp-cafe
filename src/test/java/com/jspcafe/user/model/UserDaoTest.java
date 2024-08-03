package com.jspcafe.user.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.jspcafe.exception.UserNotFoundException;
import com.jspcafe.test_util.H2Connector;
import com.jspcafe.test_util.H2Initializer;
import java.sql.SQLException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserDaoTest {

  private UserDao userDao;

  @BeforeEach
  void setUp() throws SQLException {
    H2Initializer.initializeDatabase(H2Connector.INSTANCE);
    userDao = new UserDao(H2Connector.INSTANCE);
  }

  @Test
  void email로_유저정보를_찾을_수_있다() {
    // Given
    User user = User.create("woowa@woowa.in", "김배달", "1234");

    // When
    userDao.save(user);

    // Then
    User storedUser = userDao.findByEmail("woowa@woowa.in")
        .orElseThrow(() -> new UserNotFoundException("User email not found"));
    assertNotNull(storedUser);
    assertEquals(user, storedUser);
  }

  @Test
  void id로_유저정보를_찾을_수_있다() {
    // Given
    User user = User.create("woowa@woowa.in", "김배달", "1234");

    // When
    userDao.save(user);

    // Then
    User storedUser = userDao.findById(user.id())
        .orElseThrow(() -> new UserNotFoundException("User id not found"));
    assertNotNull(storedUser);
    assertEquals(user, storedUser);
  }

  @Test
  void 저장되어있는_모든_유저정보를_찾을_수_있다() {
    // Given
    User user1 = User.create("woowa@woowa.in", "김배달", "1234");
    User user2 = User.create("coupang@co.pang", "이쿠팡", "4321");
    userDao.save(user1);
    userDao.save(user2);

    // When
    List<User> users = userDao.findAll();

    // Then
    assertEquals(2, users.size());
  }

  @Test
  void 유저정보를_업데이트_할_수_이다() {
    // Given
    User currentUser = User.create("woowa@woowa.in", "김배달", "1234");
    userDao.save(currentUser);

    // When
    User updateUser = currentUser.update("coupang@co.pang", "이쿠팡", "4321");
    userDao.update(updateUser);

    // Then
    assertEquals(currentUser.id(), updateUser.id());
    assertEquals(updateUser, userDao.findById(updateUser.id())
        .orElseThrow(() -> new UserNotFoundException("User id not found")));
    assertEquals(updateUser, userDao.findByEmail(updateUser.email())
        .orElseThrow(() -> new UserNotFoundException("User email not found")));
  }
}
