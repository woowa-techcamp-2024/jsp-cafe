<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<jsp:include page="/template/component/htmlhead.jsp"></jsp:include>
<body>
<div>
    <jsp:include page="/template/component/header.jsp"></jsp:include>
</div>

<div class="container" id="main">
    <div class="col-md-6 col-md-offset-3">
        <div class="panel panel-default content-main">
            <form name="question" method="post" action="${pageContext.request.contextPath}/users/${user.userId}">
                <div class="form-group">
                    <label for="userId">사용자 아이디</label>
                    <input class="form-control" id="userId" name="userId" placeholder="User ID" disabled value="${user.userId}">
                </div>
                <div class="form-group">
                    <label for="name">이름</label>
                    <input class="form-control" id="name" name="name" placeholder="Name" value="${user.name}">
                </div>
                <div class="form-group">
                    <label for="email">이메일</label>
                    <input type="email" class="form-control" id="email" name="email" placeholder="Email" value="${user.email}">
                </div>

                <br/>
                <br/>

                <div class="form-group">
                    <label for="password">수정을 위해 현재 <b>비밀번호</b>를 입력해주세요.</label>
                    <input type="password" class="form-control" id="password" name="password" placeholder="Password">
                </div>
                <button type="submit" class="btn btn-success clearfix pull-right">수정</button>
                <div class="clearfix"/>
            </form>
        </div>
    </div>
</div>

<div>
    <jsp:include page="/template/component/footer.jsp"></jsp:include>
</div>
</body>
</html>