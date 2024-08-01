package com.codesquad.cafe.model;

import com.codesquad.cafe.db.entity.User;
import com.codesquad.cafe.exception.ValidationException;
import com.codesquad.cafe.util.StringUtil;

public class UserJoinRequest {

    private String username;

    private String password;

    private String name;

    private String email;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public UserJoinRequest() {
    }

    public UserJoinRequest(String username, String password, String name, String email) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    public User toUser() {
        return User.of(username, password, name, email);
    }

    public void validate() {
        StringBuilder sb = new StringBuilder();
        boolean valid = true;
        if (StringUtil.isBlank(username)) {
            sb.append("username은 필수입니다.").append("\n");
            valid = false;
        }
        if (StringUtil.isBlank(password)) {
            sb.append("password는 필수입니다.").append("\n");
            valid = false;
        }
        if (StringUtil.isBlank(name)) {
            sb.append("name은 필수입니다.").append("\n");
            valid = false;
        }
        if (StringUtil.isBlank(email)) {
            sb.append("email은 필수입니다.").append("\n");
            valid = false;
        } else {
            String[] emailTokens = email.split("@");
            if (emailTokens.length != 2 || emailTokens[0].isBlank() || emailTokens[1].isBlank()) {
                sb.append("잘못된 email 형식입니다.").append("\n");
                valid = false;
            }
        }
        if (!valid) {
            throw new ValidationException(sb.toString());
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("UserJoinRequest{");
        sb.append("username='").append(username).append('\'');
        sb.append(", password='").append(password).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", email='").append(email).append('\'');
        sb.append('}');
        return sb.toString();
    }

}
