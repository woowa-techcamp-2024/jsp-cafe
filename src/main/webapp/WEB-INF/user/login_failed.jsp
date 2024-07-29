<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/components/head.jsp" %>
<body>
<%@ include file="/WEB-INF/components/header.jsp"%>
<%@ include file="/WEB-INF/components/navigation.jsp"%>

<div class="container" id="main">
   <div class="col-md-6 col-md-offset-3">
      <div class="panel panel-default content-main">
          <div class="alert alert-danger" role="alert">아이디 또는 비밀번호가 틀립니다. 다시 로그인 해주세요.</div>
          <form name="article" method="post" action="/WEB-INF/user/login.jsp">
              <div class="form-group">
                  <label for="userId">사용자 아이디</label>
                  <input class="form-control" id="userId" name="userId" placeholder="User ID">
              </div>
              <div class="form-group">
                  <label for="password">비밀번호</label>
                  <input type="password" class="form-control" id="password" name="password" placeholder="Password">
              </div>
              <button type="submit" class="btn btn-success clearfix pull-right">로그인</button>
              <div class="clearfix" />
          </form>
        </div>
    </div>
</div>

<!-- script references -->
<%@ include file="/WEB-INF/components/script.jsp" %>
	</body>
</html>