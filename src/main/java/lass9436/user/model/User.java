package lass9436.user.model;

public class User {
	private long userSeq;
	private String userId;
	private String password;
	private String name;
	private String email;

	// 기본 생성자
	public User() {}

	// 매개변수를 받는 생성자
	public User(String userId, String password, String name, String email) {
		this.userId = userId;
		this.password = password;
		this.name = name;
		this.email = email;
	}

	// Getters and Setters
	public long getUserSeq() { return userSeq; }

	public void setUserSeq(long userSeq) { this.userSeq = userSeq; }

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "User{" +
			"userSeq=" + userSeq +
			", userId='" + userId + '\'' +
			", password='" + password + '\'' +
			", name='" + name + '\'' +
			", email='" + email + '\'' +
			'}';
	}
}
