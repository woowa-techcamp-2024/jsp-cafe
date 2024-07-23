<%@ page import="com.woowa.cafe.domain.Member" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="kr">
<%@ include file="/WEB-INF/components/header.html" %>
<body>
<%@ include file="/WEB-INF/components/navbar-fixed-top-header.html" %>
<%@ include file="/WEB-INF/components/navbar-default.jsp" %>
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
                    List<Member> members = (List<Member>) request.getAttribute("members");
                    if (members != null) {
                        for (Member member : members) {
                %>
                <tr>
                    <th scope="row"><%= member.getMemberId() %>
                    </th>
                    <td><%= member.getMemberId() %>
                    </td>
                    <td><%= member.getName() %>
                    </td>
                    <td><%= member.getEmail() %>
                    </td>
                    <td><a href="<%= "/user/" + member.getMemberId()%>" class="btn btn-success" role="button">수정</a>
                    </td>
                </tr>
                <%
                        }
                    }
                %>
                </tbody>
            </table>
        </div>
    </div>
</div>

<!-- script references -->
<%@ include file="/WEB-INF/components/scirpt-refernces.html" %>
</body>
</html>
