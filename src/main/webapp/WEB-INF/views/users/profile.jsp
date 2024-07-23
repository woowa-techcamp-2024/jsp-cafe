<%@ page import="cafe.users.User" %>
<%@ include file="/WEB-INF/views/header.jsp" %>

<%
    User user = (User) request.getAttribute("user");
%>

<h2 class="mt-5">Profile</h2>
<div class="card">
    <div class="card-body">
        <h5 class="card-title">Username: <%= user.username() %>
        </h5>
        <p class="card-text">Email: <%= user.userId() %>
        </p>
    </div>
</div>

<%@ include file="/WEB-INF/views/footer.jsp" %>
