<%@ page import="cafe.users.User" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/header.jsp" %>

<div class="row justify-content-center">
    <div class="col-md-8">
        <h2 class="mt-5">User List</h2>
        <table class="table table-striped">
            <thead>
            <tr>
                <th>Username</th>
                <th>Email</th>
            </tr>
            </thead>
            <tbody>
            <%
                List<User> userList = (List<User>) request.getAttribute("userList");
                for (User user : userList) {
            %>
            <tr onclick="location.href='${pageContext.request.contextPath}/users/<%=user.id()%>'"
                style="cursor: pointer;">
                <td><%= user.username() %>
                </td>
                <td><%= user.userId() %>
                </td>
            </tr>
            <% } %>
            </tbody>
        </table>
    </div>
</div>

<%@ include file="/WEB-INF/views/footer.jsp" %>

<!-- Bootstrap Bundle with Popper -->
<script src="${pageContext.request.contextPath}/static/js/bootstrap.bundle.min.js"></script>
</body>
</html>
