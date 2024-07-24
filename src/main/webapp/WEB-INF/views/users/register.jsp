<!-- src/main/webapp/views/register.jsp -->
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/header.jsp" %>
<div class="row justify-content-center">
    <div class="col-md-6">
        <h2 class="mt-5">Sign Up</h2>
        <%
            String errorMessage = (String) request.getAttribute("errorMessage");
            if (errorMessage != null) {
        %>
        <div class="alert alert-danger"><%= errorMessage %>
        </div>
        <% } %>
        <form action="${pageContext.request.contextPath}/users" method="post">
            <div class="mb-3">
                <label for="username" class="form-label">Username</label>
                <input type="text" class="form-control" id="username" name="username"
                       value="<%= request.getParameter("username") != null ? request.getParameter("username") : "" %>"
                       required>
            </div>
            <div class="mb-3">
                <label for="email" class="form-label">Email address</label>
                <input type="email" class="form-control" id="email" name="email"
                       value="<%= request.getParameter("email") != null ? request.getParameter("email") : "" %>"
                       required>
            </div>
            <div class="mb-3">
                <label for="password" class="form-label">Password</label>
                <input type="password" class="form-control" id="password" name="password" required>
            </div>
            <div class="mb-3">
                <label for="confirmPassword" class="form-label">Confirm Password</label>
                <input type="password" class="form-control" id="confirmPassword" name="confirmPassword" required>
            </div>
            <button type="submit" class="btn btn-primary">Sign Up</button>
        </form>
    </div>
</div>
<%@ include file="/WEB-INF/views/footer.jsp" %>
