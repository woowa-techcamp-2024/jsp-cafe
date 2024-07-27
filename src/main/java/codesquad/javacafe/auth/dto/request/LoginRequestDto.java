package codesquad.javacafe.auth.dto.request;

import java.util.Map;

public class LoginRequestDto {
	private String userId;
	private String password;

	public LoginRequestDto(Map<String, String[]> map) {
		this.userId = map.get("userId")[0];
		this.password = map.get("password")[0];
	}

	public String getUserId() {
		return userId;
	}

	public String getPassword() {
		return password;
	}

	@Override
	public String toString() {
		return "LoginRequestDto{" +
			"userId='" + userId + '\'' +
			", password='" + password + '\'' +
			'}';
	}
}
