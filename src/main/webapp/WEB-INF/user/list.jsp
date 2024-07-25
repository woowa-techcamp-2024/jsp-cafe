<%@ page import="org.example.cafe.domain.User" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/base/head.jsp" %>
<%@ include file="/WEB-INF/base/header.jsp" %>
<%@ include file="/WEB-INF/base/nav.jsp" %>

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
                    List<User> users = (List<User>) request.getAttribute("users");
                    int index = 1;
                    if (users != null) {
                        for (User user : users) {
                %>

                <tr data-href="<%= "/users/" + user.getUserId() %>">
                    <th scope="row"><%= index++ %>
                    </th>
                    <td><%= user.getUserId() %>
                    </td>
                    <td><%= user.getNickname() %>
                    </td>
                    <td><%= user.getEmail() %>
                    </td>
                    <td><a href="#" class="btn btn-success" role="button">수정</a></td>
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

<script>
    document.addEventListener("DOMContentLoaded", function () {
        const rows = document.querySelectorAll("tr[data-href]");
        rows.forEach(function (row) {
            row.addEventListener("click", function () {
                window.location.href = row.getAttribute("data-href");
            });
        });
    });
</script>

<%@ include file="/WEB-INF/base/footer.jsp" %>