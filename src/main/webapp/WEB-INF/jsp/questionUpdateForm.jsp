<%--
  Created by IntelliJ IDEA.
  User: davidlee
  Date: 24. 7. 30.
  Time: 오전 1:08
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE>
<html lang="ko">
<jsp:include page="/WEB-INF/jsp/component/headers.jsp"/>
<body>
<jsp:include page="/WEB-INF/jsp/component/navbar.jsp"/>

<div class="container" id="main">
    <div class="col-md-12 col-sm-12 col-lg-10 col-lg-offset-1">
        <div class="panel panel-default content-main">
            <c:set var="articleCommonResponse" value="${requestScope.article}"/>
            <form id="question" name="question" method="post"
                  action="/questions/${articleCommonResponse.id}">
                <input type="hidden" name="id" value="${articleCommonResponse.id}"/>
                <div class="form-group">
                    <label for="title">제목</label>
                    <input type="text" class="form-control" id="title" name="title" placeholder="제목"
                           value="${articleCommonResponse.title}"/>
                </div>
                <div class="form-group">
                    <label for="contents">내용</label>
                    <textarea name="contents" id="contents" rows="5"
                              class="form-control">${articleCommonResponse.contents}</textarea>
                </div>
                <button type="submit" class="btn btn-success clearfix pull-right">수정하기</button>
                <div class="clearfix"></div>
            </form>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/jsp/component/scripts.jsp"/>
</body>
</html>

<script src="https://code.jquery.com/jquery-3.7.1.min.js"
        integrity="sha256-/JqT3SQfawRcv/BIHPThkBvs0OEvtFFmqPF/lYI/Cxo="
        crossorigin="anonymous"></script>
<script>
  $(document).ready(function () {
    $('#question').on('submit', function (e) {
      e.preventDefault(); // Prevent the form from submitting the default way

      let form = $(this);
      let actionUrl = form.attr('action');
      let formData = form.serialize(); // Serialize form data

      $.ajax({
        url: actionUrl,
        type: 'PUT', // Use PUT method
        data: formData,

        success: function () {
          window.location.href = '/questions/' + ${articleCommonResponse.id};
        },
        error: function (xhr, status, error) {
          let errorMessage = xhr.responseText
          if (confirm(status + ": " + errorMessage)) {
            window.location.href = '/questions/' + ${articleCommonResponse.id};
          }
        }
      });
    })
  });
</script>