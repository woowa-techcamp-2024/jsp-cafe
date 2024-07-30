<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/base/head.jsp" %>
<%@ include file="/WEB-INF/base/header.jsp" %>
<%@ include file="/WEB-INF/base/nav.jsp" %>


<div class="container" id="main">
   <div class="col-md-12 col-sm-12 col-lg-10 col-lg-offset-1">
      <div class="panel panel-default content-main">
          <div class="form-group">
              <label for="title">제목</label>
              <input type="text" class="form-control" id="title" name="title" placeholder="제목"
                     value="${question.title}"/>
          </div>
          <div class="form-group">
              <label for="contents">내용</label>
              <textarea name="contents" id="contents" rows="5" placeholder="내용"
                        class="form-control">${question.content}</textarea>
          </div>
          <button type="submit" class="btn btn-success clearfix pull-right"
                  onclick="clickSubmit(${question.questionId})">질문하기
          </button>
          <div class="clearfix"/>
        </div>
    </div>
</div>


<script src="/resources/js/ApiTemplate.js"></script>
<script src="/resources/js/question.js"></script>
<%@ include file="/WEB-INF/base/footer.jsp" %>