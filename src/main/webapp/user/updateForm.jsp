<%@ page import="com.example.entity.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="kr">
<%@ include file="../template/head.jsp" %>
<body>
<%@ include file="../template/top-header.jsp" %>
<%@ include file="../template/sub-header.jsp" %>
<div class="container" id="main">
    <div class="col-md-6 col-md-offset-3">
        <div class="panel panel-default">
            <div class="panel-heading"><h4>프로필 수정</h4></div>
            <div class="panel-body">
                <%
                    String userId = (String)request.getAttribute("userId");
                %>
                <form action="/users/edit/<%= userId %>" method="post">
                    <div class="form-group">
                        <label for="name">이름</label>
                        <input type="text" class="form-control" id="name" name="name" value=""
                               placeholder="변경할 이름을 입력하세요" required>
                    </div>
                    <div class="form-group">
                        <label for="email">이메일</label>
                        <input type="email" class="form-control" id="email" name="email" value=""
                               placeholder="변경할 이메일을 입력하세요" required>
                    </div>
                    <div class="form-group">
                        <label for="password">현재 비밀번호</label>
                        <input type="password" class="form-control" id="password" name="password" value=""
                               placeholder="현재 비밀번호를 입력하세요" required>
                    </div>
                    <button type="submit" class="btn btn-primary">완료</button>
                </form>
            </div>
        </div>
    </div>
</div>

<!-- script references -->
<%@include file="../template/footer.jsp" %>
</body>
</html>