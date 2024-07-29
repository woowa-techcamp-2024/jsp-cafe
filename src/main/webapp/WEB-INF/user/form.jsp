<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/components/head.jsp" %>
<body>
<%@ include file="/WEB-INF/components/header.jsp"%>
<%@ include file="/WEB-INF/components/navigation.jsp"%>

<div class="container" id="main">
   <div class="col-md-6 col-md-offset-3">
      <div class="panel panel-default content-main">
          <form name="article" method="post" action="/users/sign-up">
              <div class="form-group">
                  <label for="userid">사용자 아이디</label>
                  <input class="form-control" id="userid" name="userid" placeholder="User ID">
              </div>
              <div class="form-group">
                  <label for="password">비밀번호</label>
                  <input type="password" class="form-control" id="password" name="password" placeholder="Password">
              </div>
              <div class="form-group">
                  <label for="name">이름</label>
                  <input class="form-control" id="name" name="name" placeholder="Name">
              </div>
              <div class="form-group">
                  <label for="email">이메일</label>
                  <input type="email" class="form-control" id="email" name="email" placeholder="Email">
              </div>
              <button type="submit" class="btn btn-success clearfix pull-right">회원가입</button>
              <div class="clearfix" />
          </form>
        </div>
    </div>
</div>

<!-- script references -->
<%@ include file="/WEB-INF/components/script.jsp" %>
	</body>
</html>