package codesquad.javacafe.auth.dto.session;

public class MemberInfo {
	private long id;
	private String name;

	public MemberInfo(long id, String name) {
		this.id = id;
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "MemberInfo{" +
			"id=" + id +
			", name='" + name + '\'' +
			'}';
	}
}
