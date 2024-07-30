<%@ page contentType="text/html; charset=UTF-8" pageEncoding="utf-8" language="java" %>
<!DOCTYPE html>
<html lang="kr">
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <meta charset="utf-8">
    <title>SLiPP Java Web Programming</title>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <link href="/css/bootstrap.min.css" rel="stylesheet">
    <!--[if lt IE 9]>
    <script src="//html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->
    <link href="/css/styles.css" rel="stylesheet">
</head>
<body>

<%@include file="/WEB-INF/includes/navbar-fixed-top.jsp"%>
<%@include file="/WEB-INF/includes/navbar-default.jsp"%>

<div class="container" id="main">
   <div class="col-md-12 col-sm-12 col-lg-10 col-lg-offset-1">
      <div class="panel panel-default content-main">
          <form id="questionForm" name="question">
              <div class="form-group">
                  <label for="writer">글쓴이</label>
                  <input readonly class="form-control" id="writer" name="writer" placeholder="글쓴이" value="${sessionScope.userName}"/>
              </div>
              <div class="form-group">
                  <label for="title">제목</label>
                  <input type="text" class="form-control" id="title" name="title" placeholder="제목" value="${question.title}"/>
              </div>
              <div class="form-group">
                  <label for="contents">내용</label>
                  <textarea name="contents" id="contents" rows="5" class="form-control">${question.contents}</textarea>
              </div>
              <button type="button" class="btn btn-success clearfix pull-right" onclick="submitForm()">수정하기</button>
              <div class="clearfix" />
          </form>
        </div>
    </div>
</div>

<script>
    function submitForm() {
        const form = document.getElementById('questionForm');
        const formData = new FormData(form);
        const jsonData = {};

        formData.forEach((value, key) => {
            jsonData[key] = value;
        });

        fetch(`/questions/${question.questionSeq}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(jsonData)
        }).then(response => {
            if (response.ok) {
                // 성공 처리 (예: 페이지 리로드 또는 메시지 표시)
                window.location.href = "/questions/${question.questionSeq}"; // 예: 질문 목록 페이지로 리다이렉트
            } else {
                // 오류 처리
                alert("질문 업데이트 중 오류가 발생했습니다.");
            }
        }).catch(error => {
            console.error("Error:", error);
            alert("질문 업데이트 중 오류가 발생했습니다.");
        });
    }
</script>

<!-- script references -->
<%@include file="/WEB-INF/includes/script-references.jsp"%>
	</body>
</html>
