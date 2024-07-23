<%@ page import="codesquad.jspcafe.domain.user.payload.response.UserCommonResponse" %>
<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: KyungMin Lee
  Date: 24. 7. 22.
  Time: 오후 8:20
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE>
<html lang="ko">
<jsp:include page="/WEB-INF/jsp/component/headers.jsp"/>
<body>
<jsp:include page="/WEB-INF/jsp/component/navbar.jsp"/>
<div class="container" id="main">
    <div class="col-md-10 col-md-offset-1">
        <div class="panel panel-default">
            <table class="table table-hover">
                <thead>
                <tr>
                    <th>#</th>
                    <th>사용자 아이디</th>
                    <th>이름</th>
                    <th>이메일</th>
                    <th></th>
                </tr>
                </thead>
                <tbody>
                <%
                    @SuppressWarnings("unchecked")
                    List<UserCommonResponse> userCommonResponses = (List<UserCommonResponse>) request.getAttribute(
                            "userList");
                    int idx = 1;
                    for (UserCommonResponse commonResponse : userCommonResponses) {
                %>
                <tr>
                    <th scope="row"><%=idx++%>
                    </th>
                    <td>
                        <a href="/users/<%=commonResponse.getUserId()%>"><%=commonResponse.getUserId()%>
                        </a>
                    </td>
                    <td><%=commonResponse.getUsername()%>
                    </td>
                    <td><%=commonResponse.getEmail()%>
                    </td>
                    <td>
                        <a href="${pageContext.request.contextPath}/users/<%=commonResponse.getUserId()%>/form"
                           class="btn btn-success"
                           role="button">수정</a></td>
                </tr>
                <%
                    }
                %>
                </tbody>
            </table>
        </div>
    </div>
</div>

<!-- script references -->
<jsp:include page="/WEB-INF/jsp/component/scripts.jsp"/>
</body>
</html>
