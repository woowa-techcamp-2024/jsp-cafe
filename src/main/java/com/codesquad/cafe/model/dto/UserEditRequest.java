package com.codesquad.cafe.model.dto;

import com.codesquad.cafe.exception.ValidationException;
import com.codesquad.cafe.util.StringUtil;

public class UserEditRequest {

    private Long id;

    private String username;

    private String originalPassword;

    private String password;


    private String confirmPassword;

    private String name;

    private String email;

    public UserEditRequest() {
    }

    public UserEditRequest(Long id,
                           String username,
                           String password,
                           String originalPassword,
                           String confirmPassword,
                           String name,
                           String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.originalPassword = originalPassword;
        this.confirmPassword = confirmPassword;
        this.name = name;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getOriginalPassword() {
        return originalPassword;
    }

    public String getPassword() {
        return password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public void validate() {
        StringBuilder sb = new StringBuilder();
        boolean valid = true;
        if (id == null || id <= 0) {
            sb.append("id는 1이상의 자연수여야 합니다.").append("\n");
            valid = false;
        }
        if (StringUtil.isBlank(username)) {
            sb.append("username은 필수입니다.").append("\n");
            valid = false;
        }
        if (StringUtil.isBlank(originalPassword)) {
            sb.append("originalPassword는 필수입니다.").append("\n");
            valid = false;
        }
        if (StringUtil.isBlank(password)) {
            sb.append("password는 필수입니다.").append("\n");
            valid = false;
        }
        if (StringUtil.isBlank(confirmPassword)) {
            sb.append("confirmPassword는 필수입니다.").append("\n");
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

}
