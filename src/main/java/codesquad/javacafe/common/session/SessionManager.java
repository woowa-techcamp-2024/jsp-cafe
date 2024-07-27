package codesquad.javacafe.common.session;

import java.util.Objects;

import codesquad.javacafe.common.exception.ClientErrorCode;
import jakarta.servlet.http.HttpServletRequest;

public class SessionManager {
	private static final SessionManager instance = new SessionManager();
	private SessionManager(){};
	public static SessionManager getInstance(){
		return instance;
	}

	public void loginCheck(HttpServletRequest req, String key, String userId) {
		var memberInfo = (MemberInfo)req.getSession().getAttribute(key);
		if (memberInfo == null) {
			throw ClientErrorCode.UNAUTHORIZED_USER.customException("Unauthorized user info = "+memberInfo);
		} else if (!Objects.equals(memberInfo.getUserId(), userId)) {
			throw ClientErrorCode.ACCESS_DENIED.customException("Access Denied User Info = "+memberInfo);
		}
	}
}
